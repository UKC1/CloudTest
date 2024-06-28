module.exports = function override(config, env) {
    config.resolve.fallback = {
        ...config.resolve.fallback,
        "net": false
    };

    return config;
};

