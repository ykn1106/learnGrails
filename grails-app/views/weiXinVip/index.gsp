<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>userId转openId</title>
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <style>
        textarea{
            width:500px;
            height:600px;
        }
        th{
            width:200px;
        }
    </style>
</head>
<body>
    <input id="clientIdHidden" type="hidden" value="1"/>
    <table>
        <tr>
            <td valign="top">
                <textarea id="userIdsArea" placeholder="要转换的userId们"></textarea>
            </td>
            <td>
                <button id="submitBtn">转换</button>
            </td>
            <td valign="top">
                <table id="openIdsArea" border="1"></table>
            </td>
        </tr>
    </table>

    <script type="text/JavaScript">
        var index = 0;
        var count = 0;
        $("#submitBtn").off('click').on('click', convert);
        function convert() {
            _init();
            var arr = $("#userIdsArea").val().split("\n");
            if(arr.length == 1 && arr[0] == "") {
                alert("请输入userId!");
                return;
            }
            _recurrence(arr);
        }
        function _init() {
            index = 0;
            count = 0;
            $("#openIdsArea").html("");
        }
        function _recurrence(arr){
            if(index > arr.length-1) return;
            var userId = arr[index].trim();
            _realConvert(userId);
            setTimeout(function () {
                index++;
                _recurrence(arr);
            },10);
        }
        
        function _realConvert(userId) {
            $.ajax({
                url: "/weiXinVip/convertToOpenId",
                data: {clientId: $("#clientIdHidden").val(), userId: userId},
                success:function (data) {
                    count++;
                    if(data.success){
                        $("#openIdsArea").append("<tr><th>"+ count +"</th><th>"+userId+"</th><th>"+data.openId+"</th>+</tr>");
                        //console.log("curUserId:"+ userId +", openId:" + data.openId);
                    }else{
                        $("#openIdsArea").append("<tr><th>"+ count +"</th><th>"+userId+"</th><th>"+data.errcode+","+data.errmsg+"</th></tr>");
                        //console.log("curUserId:"+ userId +", errcode:" + data.errcode + ", errmsg:" + data.errmsg);
                    }
                }
            });

            %{--$("#openIdsArea").load("${createLink(controller: 'weiXinVip', action: 'convertToOpenId')}", {clientId: $("#clientIdHidden").val(), userId: userId}, function(data){});--}%
        }

    </script>
</body>
</html>