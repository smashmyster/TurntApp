var me = "1";
var user = "5";
var id = "24";
var event = "24";

function status(num){
  if(num == 1)
     return "Test passed";
  else
     return "Test failed"; 
}
function loginTest() {
    var us = "rey@mail.com";
    var pass = "rey";
    var type = "login";
    $.post('../index.php',
        //data to be passed
        {
            user: us,
            pass: pass,
            type: type
        },
        //function called after success
        function(data){
	    console.log(data);
            var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function upcomingEvents() {
    $.post('../index.php',
        //data to be passed
        {
	    id: id,
	  type:"get_upcoming_events"
        },
        //function called after success
        function(data) {
	    console.log(data);
         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');        }
    );
}

function ongoingEvents() {
    $.post('../index.php',
        //data to be passed
        {
            id:id,
	  type:"get_ongoing_events"
        },
        //function called after success
        function(data) {
            	    console.log(data);
         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');        }
    );
}

function userEventsAttending() {
    $.post('../index.php',
        //data to be passed
        {
           me:me,
	 user:user,
	 type:"get_user_events_attending"
        },
        //function called after success
        function(data) {
	    console.log(data);
         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');        }
    );
}

function eventsPull() {
    $.post('../index.php',
        //data to be passed
        {
           me:me,
	 type:"get_my_events"
        },
        //function called after success
        function(data) {
	    console.log(data);
         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');        }
    );
}

function userEventsHosting() {
    $.post('../index.php',
        //data to be passed
        {
           me:me,
	 user:user,
	 type:"get_user_events_hosting"
        },
        //function called after success
        function(data) {
	    console.log(data);

            var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function getUserInfo() {
    $.post('../index.php',
        //data to be passed
        {
	   me:me,
         user:user,
	 type:"get_user_info"
        },
        //function called after success
        function(data) {
	    console.log(data);
         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function eventsUserAttending() {
    $.post('../index.php',
        //data to be passed
        {
	   event:event,
	      me:me,
            type:"get_events_users_attending"
        },
        //function called after success
        function(data) {
	    console.log(data);

         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function invitableList() {
    $.post('../index.php',
        //data to be passed
        {
           event:event,
	      me:me,
            type:"get_invitable_list"
        },
        //function called after success
        function(data) {
	    console.log(data);

         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function getFollowers() {
    $.post('../index.php',
        //data to be passed
        {
            id:id,
          type:"get_my_followers"
        },
        //function called after success
        function(data) {
	    console.log(data);

         var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}

function searchFunctionsTest() {
    $.post('../index.php',
        //data to be passed
        {
           search:"Test Event",
	     type:"search_event"
        },
        //function called after success
        function(data) {
	    console.log(data);
          var obj = JSON.parse(data);
            $('#modalTitle').text(status(obj.success));
            $('#modalMsg').text(data);
            $('#id01').modal('show');
        }
    );
}
