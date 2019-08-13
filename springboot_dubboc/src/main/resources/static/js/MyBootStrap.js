
   //BootStrap js方法

 //查询    数据展示
  $(function () {
       //创建bootstrapTable
       $("#data").bootstrapTable({
           url: "../log/listlog",
			method : 'post',
			striped : true, // 斑马线效果     默认false
			//只允许选中一行
			//singleSelect : true,
			//选中行是不选中复选框或者单选按钮
			clickToSelect : true,
			showToggle : true, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			showColumns : true, //是否显示所有的列
			showRefresh : true, //是否显示刷新按钮
			minimumCountColumns : 2, //  最少留两列
			detailView : false, //是否显示父子表
			toolbar : '#tabToolBar', //  工具定义位置
			sidePagination : 'server',
			toolbar : '#toolbar',
			pagination : true, //是否展示分页
			pageList : [ 2, 4, 8, 10 ],//分页组件
			pageNumber : 1,
			pageSize : 2,//默认每页条数
			//启动回车键做搜索功能
			searchOnEnterKey : true,
			//设置为false 将禁止所有列的排序。
			sortable : true,
			//设置默认排序为 name
			sortName : 'id',
			//定义排序方式，'asc' 或者 'desc'。
			sortOrder : "desc",
			//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
			// 设置为limit可以获取limit, offset, search, sort, order  queryParamsType:'undefined',
			queryParamsType : 'undefined',
			// 请求服务器数据
			queryParams : function queryParams(params) {
				var param = {
					pageNumber : params.pageNumber,
					pageSize : params.pageSize,
					sortName : params.sortName,
					sortOrder : params.sortOrder,
					userName : $("#name").val()
				};
				return param;
			},
			columns : [
				{ field : 'checkbox',checkbox : true }, //开启复选框
			    { field : 'logip', title : 'ip地址', width : 300},
			    { field : 'logdate', title : '时间',width : 300 },
			    { field : 'setrequerpath', title : '请求地址', width : 300 },
			    
			    { field : 'logname', title : '日志名称', width : 300 },
			    { field : 'userid', title : '用户名称', width : 300 },
			    { field : 'parame', title : '参数', width : 300 },

			    { field : 'x', title : '操作', width : 300,
			    	formatter:function(value,row,index){
                        var id=JSON.stringify(row.id);  //id为string长字符串 要转json传参
			    		return  "<a href='javascript:delete1(id)' >删除</a>";
			    	}
			    
			    }
			    
			 ]
		});
 })
	
	//条件查询     使用parameUtil工具类
	function sousuo() {
		$("#data").bootstrapTable("refresh", {
			"query":{
			"userName" : $("#logname").val(),
			}
		});

	}
	
	
   //删除  批量删除
	 function delete1() {
	     var arr = $('#data').bootstrapTable("getSelections");//获取选中的行
	     console.log(arr);	     
	     if (arr.length <= 0) {
	         alert("请选择要删除的数据！");
	         return ;	         
	     }
	         var ids = "";
	         for (var i = 0; i < arr.length; i++) {
	             ids += arr[i].id + ",";
	         }
	         
	         ids = ids.substring(0, ids.length - 1);
	       if(confirm("您确定删除这"+arr.length+"条数据吗？")){
	         $.ajax({
	             url: "../log/delete1",
	             data: {ids: ids},
	             type: "post",
	             success: function () {
	                 alert("删除成功");
	                 $("#data").bootstrapTable("refresh");

	             }, error: function () {
	                 alert("失败！");
	             }
	         })	
	      }
	 }
	 
	 
	 
 
	//修改  打开弹框同时回显
	   update2=function(){
		 var array = $('#data').bootstrapTable("getSelections");//获取选中的行  数组
			var  arr=array[0];  //转化对象
		 if(array.length != 1){  //判断数组长度 为一条时回显
			 alert("选择一条要修改的数据");
		 }else{
			
			  //打开弹框
			 $("#myModal2").modal("show"); 
			 //回显

		     $("#id2").val(arr.id);
		     $("#logname").val(arr.logname);
			 $("#logip").val(arr.logip);
			 $("#logdate").val(arr.logdate);
			 $("#parame").val(arr.parame);

			 
		 }
		}
		
	   
	   
		//修改或新增   提交
		tijiao=function(a){   //a=1新增的  2修改的
			   $.ajax({
	               url: '../log/addlog',
	               type: 'post',
	               data: $("#form"+a).serialize(),
	               success: function () {
	            	   $('#myModal'+a).modal('hide'); //隐藏 (执行完关闭弹框)
	            	   $("#form"+a)[0].reset();  //清除form表单
	            	   //$('#myModal').modal('toogle'); //开关
	                   $('#data').bootstrapTable("refresh");  //refresh刷新当前页
	                   
	               }, error: function () {
	                   alert("失败！");
	               }
	           });
		}
		
