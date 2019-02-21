$(document).ready(function(){
    var prizeInfArr = ["1", "2", "3", "4", "5", "6",
                        "7", "8", "9", "10", "11", "12",
                        "13", "14"];
    $('#submitBtn').on("click",function() {
        var result = {};
        result.numberOfTimes = $("#noOfTimes").val();
        result.type = $("#type").val();
        result.resultFor = $("#resultFor").val();
        result.prizes = [];
        for(i = 1; i<= 14; i++) {
            
            $(".prize"+i).children('div').children("input").each(function() {
                var prize = {};
                prize.title = prizeInfArr[i-1];
                prize.codePoint = $(this).val();
                result.prizes.push(prize);
            });
            
        }
        console.log(result);
        console.log(JSON.stringify(result));
        
        $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            beforeSend: function(request) {
              request.setRequestHeader("Authorization", $("#passCode").val());
            },
            url: "/MyanmarLottery/lottery/add",
            data: JSON.stringify(result),
            processData: false,
            success: function(msg) {
              alert(msg);
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            },
            dataType: "json"
        });
    });
});