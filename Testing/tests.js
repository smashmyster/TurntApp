function loginTest() {
    var user = "someuser";
    var pass = "password";
    var type = "login";
    $.post('../index.php',
        //data to be passed
        {
            user: user,
            pass: pass,
            type: type
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function upcomingEvents() {
    $.post('../index.php',
        //data to be passed
        {
	    id:"someId",
	  type:"get_upcoming_events"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function ongoingEvents() {
    $.post('../index.php',
        //data to be passed
        {
            id:"someId",
	  type:"get_ongoing_events"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function userEventsAttending() {
    $.post('../index.php',
        //data to be passed
        {
           me:"myId",
	 user:"otherUserId",
	 type:"get_user_events_attending"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function eventsPull() {
    $.post('../index.php',
        //data to be passed
        {
           me:"myId"
	 type:"get_my_events"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function userEventsHosting() {
    $.post('../index.php',
        //data to be passed
        {
           me:"myId",
	 user:"otherUserId",
	 type:"get_user_events_hosting"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function getUserInfo() {
    $.post('../index.php',
        //data to be passed
        {
	   me:"myId",
         user:"otherUser",
	 type:"get_user_info"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function eventsUserAttending() {
    $.post('../index.php',
        //data to be passed
        {
	   event:"eventID",
	      me:"myId"
            type:"get_events_users_attending"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function invitableList() {
    $.post('../index.php',
        //data to be passed
        {
           event:"eventId",
	      me:"myID",
            type:"get_invitable_list"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function getFollowers() {
    $.post('../index.php',
        //data to be passed
        {
            id:"myId",
          type:"get_my_followers"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}

function searchFunctionsTest() {
    $.post('../index.php',
        //data to be passed
        {
           search:"SearchHere",
	     type:"search_event"
        },
        //function called after success
        function(data) {
            window.alert(data);
        }
    );
}
