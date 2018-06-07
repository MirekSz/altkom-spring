$(document).ready(function() {
	// console.log('dzialam')
	//	
	// $.get('http://localhost:8080/shop/api/products?query=sa', function(data){
	// $("table tbody").empty();
	// console.log(data)
	// for(let el of data){
	// $("table tbody").append(`<tr>
	// <td>${el.name}</td>
	// <td>${el.quantity}</td>
	// <td>${el.price}</td>
	// </tr>`);
	// }
	// })
 
//	$.get('http://localhost:8080/shop/product/list-as-rows', function(data) {
//		$("table tbody").empty();
//		$("table tbody").append(data);
//	})
	
	var table= $('#productsTable').DataTable( {
	        "ajax": "/spring-shop/api/products/ds",
	        "serverSide": true,
	        "select": true,
	        "dom": 'Bfrtip',
	        "buttons": [
	            {
	                text: 'Get selected data',
	                action: function () {
	                    var count = table.rows( { selected: true } ).count();
	                    var count = table.row().data().id;
	                }
	            }
	        ],
	        "columns": [
	            { "data": "id" },
	            { "data": "name" },
	            { "data": "quantity" },
	            { "data": "price" },
	            {
	                 sortable: false,
	                 "render": function ( full, type, data, meta ) {
	                	 return '<a href="'+data.id+'/edit"> <i class=" glyphicon glyphicon-pencil"></i></a>'
	                 }
	             },
	        ]
	    } );
	 
})