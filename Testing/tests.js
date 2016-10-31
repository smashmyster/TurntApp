var me = "1";
var user = "5";
var id = "24";
var event = "24";

function status(data){
  var obj;
   try{
        obj=JSON.parse(data);
        if(obj.success == 1){
    	   $('#modalTitle').text("Test passed!");
           $('#modalMsg').text(data);
     	   $('#id01').modal('show');
         }else{
           $('#modalTitle2').text("Test failed!");
           $('#modalMsg2').text(obj.message);
           $('#id02').modal('show');
         } 
    }catch(e){
           $('#modalTitle2').text("Test failed!");
           $('#modalMsg2').text("Error parsing JSON");
           $('#id02').modal('show');
    }
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
	   regid:"klsjflk",
            type: type
        },
        //function called after success
        function(data){
	console.log(data);
           status(data);
        }
    );
}

function upcomingEvents() {
    $.post('../index.php',
        //data to be passed
        {
	    id: "0",
	   user:user,
	  type:"get_upcoming_events"
        },
        //function called after success
        function(data) {
	console.log(data);
           status(data);       
}
    );
}

function ongoingEvents() {
    $.post('../index.php',
        //data to be passed
        {
  	    id: "0",
	   user:user,
	  type:"get_ongoing_events"
        },
        //function called after success
        function(data) {
	console.log(data);
           status(data);
  }
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
           status(data);
	}
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
           status(data);       }
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
           status(data);
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
           status(data);
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
           status(data);
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
           status(data);
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
           status(data);
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
             status(data);
        }
    );
}
