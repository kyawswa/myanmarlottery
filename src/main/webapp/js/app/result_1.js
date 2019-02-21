$(document).ready(function(){
    var old_prizeInfArr = ["1500", "1000", "500", 
						"300", "200", "100",
						"50", "25",	"10", 
						"5", "3", "2", "1",
						"0.5",
                                                "10,000", "1500", "1000", 
						"500", "300", "200",
						"100", "50",	"25", 
						"10", "5", "3", "2",
    						"0.5"]; // 0.5 mean 50,000
                                            
    function createResult(start, end) {
        var result = {};
        
        result.prizes = [];
        var count = 1;
        for(i = start; i<= end; i++) {
            var prize = {};
            prize.prizeInfo = {prizeCode: old_prizeInfArr[i-1]};
                
            prize.items = [];
            $(".prize"+i).children('div').children("input").each(function() {
                var j = 0;
                var codeNo = $(this).val();
				
                var arr = codeNo.split("-");
                var item = {word:arr[0], code:arr[1]};
                console.log(item);
                
                prize.items.push(item);
                j++;
                count++;
            }
            );
            result.prizes.push(prize);
        }
        
        console.log("CountLength:"+count);
        console.log("PrizeLenght:"+result.prizes.length);
        console.log(result);
        return result;
    }
    $('#submitBtn').on("click",function() {
        var resultArr = [];
        
        
        var result1 = createResult(1, 14);
        result1.numberOfTimes = $("#noOfTimes").val();
        result1.type = $("#type").val();
        result1.resultFor = $("#resultFor").val();
        
        var result2 = createResult(15, 28);
        result2.numberOfTimes = $("#noOfTimes2").val();
        result2.type = $("#type2").val();
        result2.resultFor = $("#resultFor2").val();
        
        resultArr[0] = result1;
        resultArr[1] = result2;
        
        console.log(JSON.stringify(resultArr));
        
//        $.ajax({
//            type: "POST",
//            contentType: "application/json;charset=utf-8",
//            beforeSend: function(request) {
//              request.setRequestHeader("Authorization", $("#passCode").val());
//            },
//            url: "/MyanmarLottery/lottery/add",
//            data: JSON.stringify(result),
//            processData: false,
//            success: function(msg) {
//              alert(msg);
//            },
//            error: function(xhr, status, error) {
//                alert(xhr.responseText);
//            },
//            dataType: "json"
//        });
    });
});