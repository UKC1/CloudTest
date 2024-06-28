package com.cookshare.chat.exception;

import org.springframework.dao.DataAccessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbErrorHandlingExecutor {
	private static final String DATABASE_UNAVAILABLE_MESSAGE = "데이터베이스 서비스를 사용할 수 없습니다. 나중에 다시 시도해주세요.";

	public static void executeDatabaseOperation(Runnable dbOperation, String errorMessage) {
		try {
			dbOperation.run();
		} catch (DataAccessException e) {
			log.error("데이터베이스 접근 오류가 발생했습니다: " + errorMessage, e);
			throw new DatabaseServiceUnavailableException(DATABASE_UNAVAILABLE_MESSAGE, e);
		} catch (Exception e) {
			log.error(errorMessage, e);
			throw new ApplicationProcessingException(errorMessage, e);
		}
	}



	public static void handleUpdateFailure(String userId, String chatRoomId) {
		log.error("업데이트 작업 실패 - 문서를 찾고 업데이트하지 못했습니다. 사용자 ID: {}, 채팅방 ID: {}", userId, chatRoomId);

	}
}


