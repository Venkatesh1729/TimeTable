$(document).ready(function(){
	$('#multi-select-class').dropdown();
	$('#multi-select-subject').dropdown();
	$('#multi-select-class-period').dropdown();
	$('#multi-select-subject-period').dropdown();
	$('#select-day').dropdown();
	$('#select-period').dropdown();
	$('#select-week').dropdown();
	$('#select-noon').dropdown();
	$('#teacherInfoEdit').hide();
	$('#finishOrEditTeacherInfo').hide();
	$('#classWithTeacher').hide();
	$('#finishOrEditClassWithTeacher').hide();
	$('#editBasicInfo').hide();
	$('#preferenceOfTeacher').hide();
	$('#finishOrEditPreferenceOfTeacher').hide();
	$('#exportFile').hide();
	$('#generateExportDiv').hide();
	
	var classArray = {};
	var teacherArray = {};
	var classTeacherArray = {};
	
	var jsonObject = new Object();
	jsonObject.className = "venkatesh";
	
	$("#main-content").css('display','none');
	$("#loader").css('display','block');
	
	$.ajax({
		
		type: 'POST',
	    contentType: 'application/json',
	    url: '/TimeTable/rest/timetableResource/get/details',
	    dataType: 'json',
	    data:JSON.stringify(jsonObject),
		success: (function(data,response){
			
			classArray = data.CLASS_ARRAY;
			teacherArray = data.TEACHER_ARRAY;
			classTeacherArray = data.CLASS_TEACHER_ARRAY;
			
			for(key in data.CLASS_ARRAY){
				$('#multi-select-class').append($('<option>', {value:key, text:data.CLASS_ARRAY[key]}));
			}
			for(key in data.TEACHER_ARRAY){
				$('#multi-select-subject').append($('<option>', {value:key, text:data.TEACHER_ARRAY[key]}));
			}
			
			for(key in data.CLASS_TEACHER_ARRAY){
				$('#multi-select-class-period').append($('<option>', {value:key, text:data.CLASS_ARRAY[key]}));
			}
			
			$("#main-content").css('display','block');
			$("#loader").css('display','none');
			console.log("success");
		}),
		error : function(data,response) {
			$("#main-content").css('display','block');
			$("#loader").css('display','none');
			console.log("error");
		}
	});
	
	$('#finishBasicInfo').click(function(){
		$("#basicInfoEdit").slideUp();
		$('#finishBasicInfo').hide();
		$('#editBasicInfo').show();
		
		$('#teacherInfoEdit').slideDown();
		$('#finishOrEditTeacherInfo').show();
		$('#finishTeachInfo').show();
		$('#editTeachInfo').hide();
		
	});
	
	$('#editBasicInfo').click(function(){
		$("#basicInfoEdit").slideDown();
		$('#editBasicInfo').hide();
		$('#finishBasicInfo').show();
		
		$('#teacherInfoEdit').slideUp();
		$('#finishOrEditTeacherInfo').hide();
		
		$('#classWithTeacher').slideUp();
		$('#finishOrEditClassWithTeacher').hide();
		
		$('#preferenceOfTeacher').slideUp();
		$('#finishOrEditPreferenceOfTeacher').hide();
		
		$('#exportFile').slideUp();
		$('#generateExportDiv').hide();
		
	});
	
	$('#finishTeachInfo').click(function(){
		$("#teacherInfoEdit").slideUp();
		$('#finishTeachInfo').hide();
		$('#editTeachInfo').show();
		
		
		$('#classWithTeacher').slideDown();
		$('#finishOrEditClassWithTeacher').show();
		$('#finishClassWithTeacher').show();
		$('#editClassWithTeacher').hide();
		
	});
	
	$('#editTeachInfo').click(function(){
		$("#teacherInfoEdit").slideDown();
		$('#editTeachInfo').hide();
		$('#finishTeachInfo').show();
		
		$('#classWithTeacher').slideUp();
		$('#finishOrEditClassWithTeacher').hide();
		
		$('#preferenceOfTeacher').slideUp();
		$('#finishOrEditPreferenceOfTeacher').hide();
		
		$('#exportFile').slideUp();
		$('#generateExportDiv').hide();
	});
	
	$('#finishClassWithTeacher').click(function(){
		$("#classWithTeacher").slideUp();
		$('#finishClassWithTeacher').hide();
		$('#editClassWithTeacher').show();
		
		$('#preferenceOfTeacher').slideDown();
		$('#finishOrEditPreferenceOfTeacher').show();
		$('#finishPreferenceOfTeacher').show();
		$('#editPreferenceOfTeacher').hide();
		
	});
	
	$('#editClassWithTeacher').click(function(){
		$("#classWithTeacher").slideDown();
		$('#editClassWithTeacher').hide();
		$('#finishClassWithTeacher').show();
		
		$('#preferenceOfTeacher').slideUp();
		$('#finishOrEditPreferenceOfTeacher').hide();
		
		$('#exportFile').slideUp();
		$('#generateExportDiv').hide();
	});
	
	$('#finishPreferenceOfTeacher').click(function(){
		$("#preferenceOfTeacher").slideUp();
		$('#finishPreferenceOfTeacher').hide();
		$('#editPreferenceOfTeacher').show();
		
		$("#exportFile").slideDown();
		$('#generateExportDiv').show();
		
	});
	
	$('#editPreferenceOfTeacher').click(function(){
		$("#preferenceOfTeacher").slideDown();
		$('#finishPreferenceOfTeacher').show();
		$('#editPreferenceOfTeacher').hide();
		
		$("#exportFile").slideUp();
		$('#generateExportDiv').hide();
	});
	
	$('#generateExportButton').click(function(){
		
		var jsonObject = new Object();
		jsonObject.fileName = "E:\\TimeTable\\TimeTable.xlsx";
		
		$("#main-content").css('display','none');
		$("#loader").css('display','block');
		
		$.ajax({
			
			type: 'POST',
		    contentType: 'application/json',
		    url: '/TimeTable/rest/timetableResource/generate/timetable',
		    dataType: 'json',
		    data:JSON.stringify(jsonObject),
			success: (function(data,response){
				$("#main-content").css('display','block');
				$("#loader").css('display','none');
				console.log("success");
			}),
			error : function(data,response) {
				$("#main-content").css('display','block');
				$("#loader").css('display','none');
				console.log("error");
			}
		});
	});
	
	$("#addBasicInfo").click(function(){
		
		var className = $("#className").val();
		var noOfDays = $("#noOfDays").val();
		var noOfPeriods = $("#noOfPeriods").val();
		
		if(!hasValue(className)){
			$("#message").html('<div class="ui warning message">Please enter the class name!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(!hasValue(noOfDays)){
			$("#message").html('<div class="ui warning message">Please enter the no of days!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(!hasValue(noOfPeriods)){
			$("#message").html('<div class="ui warning message">Please enter the no of periods!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(hasValue(className) && hasValue(noOfDays) && hasValue(noOfPeriods)){
			
			var jsonObject = new Object();
			jsonObject.className = className;
			jsonObject.noOfDays = noOfDays;
			jsonObject.noOfPeriods = noOfPeriods;
			$("#main-content").css('display','none');
			$("#loader").css('display','block');
			$.ajax({
				
				type: 'POST',
			    contentType: 'application/json',
			    url: '/TimeTable/rest/timetableResource/save/class',
			    dataType: 'json',
			    data:JSON.stringify(jsonObject),
				success: (function(data,response){
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("success");
					if(data != undefined && data.id != undefined && data.id > 0){
						$('#multi-select-class').append($('<option>', {value:data.id, text:className}));
					}
				}),
				error : function(data,response) {
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("error");
				}
			});
		}
		
	});
	
	$("#clearBasicInfo").click(function(){
		
	});
	
	$("#addTeacherInfo").click(function(){
		
		var teacherName = $("#teacherName").val();
		var subjectName = $("#subjectName").val();
		
		if(!hasValue(teacherName)){
			$("#message").html('<div class="ui warning message">Please enter the teacher name!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(!hasValue(subjectName)){
			$("#message").html('<div class="ui warning message">Please enter the subject name!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(hasValue(teacherName) && hasValue(subjectName)){
			
			var jsonObject = new Object();
			jsonObject.teacherName = teacherName;
			jsonObject.subjectName = subjectName;
			$("#main-content").css('display','none');
			$("#loader").css('display','block');
			$.ajax({
				
				type: 'POST',
			    contentType: 'application/json',
			    url: '/TimeTable/rest/timetableResource/save/teacher',
			    dataType: 'json',
			    data:JSON.stringify(jsonObject),
		
				success: (function(data,response){
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("success");
					if(data != undefined && data.id != undefined && data.id > 0){
						$('#multi-select-subject').append($('<option>', {value:data.id, text:teacherName+"("+subjectName+")"}));
					}
				}),
				error : function(data,response) {
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("error");
				}
			});
		}
		
	});
	
	$("#clearTeacherInfo").click(function(){
		
	});
	
	$("#addTeacherWithClass").click(function(){

		var classId = $('#multi-select-class').val();
		var teacherArray = $('#multi-select-subject').val();
		
		if(!hasValue(classId)){
			$("#message").html('<div class="ui warning message">Please enter the class name!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(!hasValue(teacherArray)){
			$("#message").html('<div class="ui warning message">Please enter the subject name!</div>');
			$(".warning").fadeOut(3000);
			
		}else if(hasValue(classId) && hasValue(teacherArray)){
			
			var jsonObject = new Object();
			jsonObject.classId = classId;
			jsonObject.teacherArray = teacherArray;
			$("#main-content").css('display','none');
			$("#loader").css('display','block');
			$.ajax({
				
				type: 'POST',
			    contentType: 'application/json',
			    url: '/TimeTable/rest/timetableResource/save/classteacher',
			    dataType: 'json',
			    data:JSON.stringify(jsonObject),
				success: (function(data,response){
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("success");
				}),
				error : function(data,response) {
					$("#main-content").css('display','block');
					$("#loader").css('display','none');
					console.log("error");
				}
			});
		}
	});
	
	$("#clearTeacherWithClass").click(function(){
		
	});
	
	$("#addPreferenceOfTeacher").click(function(){
		
	});
	
	$("#clearPreferenceOfTeacher").click(function(){
		
	});
	
	$("#multi-select-class-period").change(function(){
		
		$("#main-content").css('display','none');
		$("#loader").css('display','block');
		
		var classKey = $("#multi-select-class-period").val();
		
		for(key in classTeacherArray[classKey]){
			$('#multi-select-subject-period').append($('<option>', {value:classTeacherArray[classKey][key], text:teacherArray[classTeacherArray[classKey][key]]}));
		}
		
		$("#main-content").css('display','block');
		$("#loader").css('display','none');
	})
	function hasValue(elem) {
		return elem.length > 0;
	}
});