var exec = require('cordova/exec');

var securityAPI = {};

/**
 * 生成公私钥
 * @param {*} arg0 
 * @param {*} success 
 * @param {*} error 
 */
securityAPI.genKeyPairMethod = function (arg0, success, error) {
    exec(success, error, "SecurityUtil", "genKeyPairMethod", [arg0]);
};

/**
 * 获取公钥
 * @param {*} arg0 
 * @param {*} success 
 * @param {*} error 
 */
securityAPI.getPublicKeyMethod = function (arg0, success, error) {
    exec(success, error, "SecurityUtil", "getPublicKeyMethod", [arg0]);
};

/**
 * 签名
 * @param {*} arg0 
 * @param {*} success 
 * @param {*} error 
 */
securityAPI.signMethod = function (arg0, arg1, success, error) {
    exec(success, error, "SecurityUtil", "signMethod", [arg0, arg1]);
};
/**
 * 验签
 * @param {*} arg0 
 * @param {*} arg1 
 * @param {*} success 
 * @param {*} error 
 */
securityAPI.verifyMethod = function (arg0, arg1, arg2,success, error) {
    exec(success, error, "SecurityUtil", "verifyMethod", [arg0, arg1,arg2]);
};

module.exports = securityAPI;
