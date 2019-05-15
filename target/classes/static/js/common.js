//展示loading
function g_showLoading(){
    var  idx = layer.msg('处理中',{icon:16,shade:[0.5,'#f5f5f5'],scrollbar:false,offset:'0px',time:10000})
    return idx;
}
//salt值
var g_password_salt = "abcd1234";