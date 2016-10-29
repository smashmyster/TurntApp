//This will be sample manager
function create_society() {
    var localObj = JSON.parse(localStorage.getItem("userInfo"));
    var user = localObj.id;
    var type = 'create_group';
    var name = $('#societyName').val();
    var nasasa_reg = $('#nasasa_registered :selected').text();
    var nasasa_id = $('#NID').val();
    var bank = $('#Bank :selected').text();
    var account_number = $('#account').val();
    var reg_date = $('#date').val();
    var soc_type = $('#sType').val();
    $.post('destination.php', {
            name: name,
            user: user,
            type: type,
            nasasa_reg: nasasa_reg,
            nasasa_id: nasasa_id,
            bank: bank,
            account_number: account_number,
            reg_date: reg_date,
            soc_type: soc_type
        },
        function(data) {
            console.log(data);
            var received = JSON.parse(data);
            if (received.success == 1) {
                var group_id = received.group_id;
                localStorage.setItem("group_id", group_id);
                location.replace("ActiveMember.htm");
            }
        }
    );
}

function login() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var type = "login";
    $.post('destination.php',
        //data to be passed
        {
            user: user,
            pass: pass,
            type: type
        },
        //function called after success
        function(data) {
            console.log(data);
            localStorage.setItem("userInfo", data);
            localStorage.setItem("logged_in", "1");
            var obj = JSON.parse(data);
            if (obj.success == 1) {
                window.location.href = 'ShowHome.htm';
            } else {
                $('#myModal').modal('show');
            }
        }
    );
}

function signUp() {
    var user = $('#cell').val();
    var pass = $('#password').val();
    var name = $('#name').val();
    var surname = $('#surname').val();
    var id_number = $('#ID').val();
    var type = "sign_up";
    $.post('destination.php',
        //data to be passed
        {
            user: user,
            pass: pass,
            name: name,
            surname: surname,
            id_number: id_number,
            type: type
        },
        //function called after success
        function(data) {
            console.log(data);
            window.location.href = 'NewAdmin.htm';
        }
    );

}

function Search() {
    var name = localStorage.getItem("searchResults");
    // alert(name);
    var type = 'search_societies';
    $.post('destination.php', {
            name: name,
            type: type
        },
        function(data) {
            /* alert(data);
             console.log(data);*/

            var obj = JSON.parse(data);
            if (obj.success == 0) {
                $('#noResultsModal').modal('show');
            } else {
                $("#societyResults tr").remove();
                for (var i = 1; i <= obj.data.length; i++) {
                    var group_name = obj.data[i - 1].name;
                    var group_ID = obj.data[i - 1].id;
                    // console.log(group_data);
                    //This just inflates the data into the table
                    var group_id = "I" + i;
                    $("#societyResults tbody").append(
                        "<tr id=" + group_id + ">" +
                        "<td id=" + i + " class='td_all' >" + group_ID + "</td>" +
                        "<td>" + group_name + "</td>");
                    $('#societyResults').on('click', '#' + group_id, function() {
                        var localObj = JSON.parse(localStorage.getItem("userInfo"));
                        var user = localObj.id;
                        var group = this.cells[0].innerHTML;
                        //alert('clicked');
                        var type = "request_membership";
                        $.post('destination.php',
                            //data to be passed
                            {
                                user: user,
                                group: group,
                                type: type
                            },
                            //function called after success
                            function(data) {
                                $('#searchModal').modal('show');
                            }
                        );
                    });
                }
                /*$('.td_all').hide();
                $('#searchModal').modal('show');*/
            }
        }
    )
}

function results() {
    var name = $('#searchText').val();
    localStorage.setItem("searchResults", name);
    window.location.href = 'searchHistory.htm';
}

function DisplaySocieties() {


}

function BorrowedFunds() {
    var group_id = localStorage.getItem("group_id");
    // localStorage.setItem("group_name");
    //  var localObj =localStorage.getItem("group_data");
    //  var group = localObj.id;
    var type = 'borrowed_history';
    $.post('destination.php', {
            group: group_id,
            type: type
        },
        function(data) {
            var obj = JSON.parse(data);
            var name = obj.person[0].name;
            for (var i = 0; i < obj.person.length; i++) {
                $("#borrowedFundsTable tbody").append(
                    "<tr>" +
                    "<td>" + obj.person[i].name + "</td>" +
                    "<td>" + obj.person[i]['transaction'].approved + "</td>" +
                    "<td>" + obj.person[i]['transaction'].amount + "</td>" +
                    "<td>" + obj.person[i]['transaction'].payback_date + "</td>" +
                    "<td>" + obj.person[i]['transaction'].payback_type + "</td>" +
                    "<td>" + obj.person[i]['transaction'].interest + "</td>" +

                    "</tr>"
                );
            }
        }
    );

}

function requestFunds() {
    var localObj = JSON.parse(localStorage.getItem("userInfo"));
    var user = localObj.id;
    var group = localStorage.getItem("group_id");
    var amount = $('#amount').val();
    var period = $('#period').val(); //period not in database
    var pay_date = $('#date').val();
    var pay_type = $('#paybackType :selected').text();
    var interest = "20%"; //this has to be added to the front end
    var type = "request_funds";

    $.post('destination.php', {
            user: user,
            amount: amount,
            group: group,
            pay_date: pay_date,
            pay_type: pay_type,
            interest: interest,
            type: type
        },
        function(data) {
            // alert(data);
            console.log(data);
        }
    );
}

function societyProfile() {
    var group = localStorage.getItem("group_id");
    var type = 'get_society_profile';
    $.post('destination.php', {
            group: group,
            type: type
        },
        function(data) {
            // alert(data);
            var obj = JSON.parse(data);
            var name = obj.data[0].name;
            for (var i = 0; i < obj.data.length; i++) {
                $("#societyProfileTable tbody").append(
                    "<tr>" +
                    "<td>" + obj.data[i].name + "</td>" +
                    "<td>" + obj.data[i].surname + "</td>" +
                    "<td>" + obj.data[i].joined + "</td>" +
                    "</tr>"
                );
            }
        }
    );
}

function myPayments() {
    var localObj = JSON.parse(localStorage.getItem("userInfo"));
    var user = localObj.id;
    var group = localStorage.getItem("group_id");
    var type = 'my_payments';
    $.post('destination.php', {
						user:user,
						group: group,
            type: type
        },
        function(data) {
            // alert(data);
           var obj = JSON.parse(data);
           var name = obj.data[0].name;
            for (var i = 0; i < obj.data.length; i++) {
                $("#myPaymentsTable tbody").append(
                    "<tr>" +
                    "<td>" + obj.data[i].group_name + "</td>" +
                    "<td>" + obj.data[i].amount + "</td>" +
                    "</tr>"
                );
            }
        }
    );
}

function societyPayments() {
    var group_id = localStorage.getItem("group_id");
    // localStorage.setItem("group_name");
    //  var localObj =localStorage.getItem("group_data");
    //  var group = localObj.id;
    var type = 'society_payments';
    $.post('destination.php', {
            group: group_id,
            type: type
        },
        function(data) {
            // alert(data);
            var obj = JSON.parse(data);
            var name = obj.data[0].user_name;
            for (var i = 0; i < obj.data.length; i++) {
                $("#borrowedFundsTable tbody").append(
                    "<tr>" +
                    "<td>" + obj.data[i].user_name + "</td>" +
                    "<td>" + obj.data[i].amount + "</td>" +
                    "<td>" + obj.message + "</td>" +

                    "</tr>"
                );
            }
        }
    );



}

function groupName() {
    var groupInfo = localStorage.getItem("group_name");
    // alert(groupInfo);
    $('#displayName').text(groupInfo);
}

function meeting() {
    var group_id = localStorage.getItem("group_id");
    var date = $('#date').val();
    var time = $('#time').val();
    var venue = $('#venue').val();
    var type = 'create_meeting'
    var message = $("#meeting_description").val();
    $.post('destination.php', {
            group: group_id,
            date: date,
            time: time,
            venue: venue,
            type: type,
            message: message

        },
        //funciton called after success
        function(data) {
            console.log(data);
            var get = JSON.parse(data);
            if (get.success == 1) {
                $("#popup").text(get.message);
                $('#myModal').modal('show');
                //  window.history.back();
            } else {
                $("#popup").text(get.message);
                $('#myModal').modal('show');
            }
        }
    );


}
$(function user_login() {
    //
    //Real time searching
    //TODO:Store last result and show it when the user presses the search button
    $('#search').click(function() {

    });
    $('#searchText').on("change keyup paste", function() {
        var name = $('#searchText').val();
        var type = 'search_societies';
        $.post('destination.php', {
                name: name,
                type: type
            },
            function(data) {
                console.log(data);
                console.log(JSON.parse(data));
                var results = JSON.parse(data);
                var tags = [];
                if (results.data) {
                    for (var i = 1; i < results.data.length; i++) {
                        tags.push(results.data[i].name);
                    }
                    $("#searchText").autocomplete({
                        source: tags
                    });
                }
            }
        )
    });
    //This one sets group name under active member
    /*  $(function(){
        var groupInfo = JSON.parse(localStorage.getItem("group_data"));
        $('#displayName').text = groupInfo.name;
      });*/


});

function load_my_groups() {
    //This function deals with displaying societies users are involved in
    // if($('.body').is('AdminView')){
    var localObj = JSON.parse(localStorage.getItem("userInfo"));
    var user = localObj.id;
    var type = 'my_groups';
    $.post('destination.php', {
            user: user,
            type: type
        },
        //funciton called after success
        function(data) {
            // console.log(data);
            var obj = JSON.parse(data);
            //alert(data);
            if (obj.data.length) {
                for (var i = 1; i <= obj.data.length; i++) {
                    var group_name = obj.data[i - 1]["group_data"].name;
                    var group_ID = obj.data[i - 1]["group_data"].id;
                    //console.log(data);
                    //This just inflates the data into the table
                    var group_id = "I" + i;
                    $("#societyTable tbody").append(
                        "<tr id=" + group_id + ">" +
                        "<td id=" + i + " class='td_all' >" + group_ID + "</td>" +
                        "<td>" + group_name + "</td>");
                    $('#societyTable').on('click', '#' + group_id, function() {
                        var group_id = this.cells[0].innerHTML;
                        var group_name = this.cells[1].innerHTML;
                        localStorage.setItem("group_id", group_id);
                        localStorage.setItem("group_name", group_name);
                        var user_id = JSON.parse(localStorage.getItem("userInfo")).id;
                        $.post('destination.php', {
                                type: "is_admin",
                                id: user_id,
                                group: group_id
                            },
                            function(confirm) {
                                console.log(confirm);
                                var check = JSON.parse(confirm);
                                if (check.success == 1) {
                                    window.location.href = 'Admin.htm';
                                } else {
                                    window.location.href = 'Member.htm';
                                }
                            });
                    });
                }
            } else {
                $("#societyTable tbody").append(
                    "<center><tr><td>No Societies to show</td></tr></center>");
            }
            $('.td_all').hide();
        }
    );
    // }
}

function logout() {
    localStorage.setItem("userInfo", "");
    localStorage.setItem("logged_in", "0");
    localStorage.setItem("group_id", "-1");
    location.replace("index.htm");
}

function check_login() {
    var check = localStorage.getItem("logged_in");
    if (check == 0) {
        location.replace("index.htm");
    }
    set_modal();
}

function click_message(id) {
    localStorage.setItem("group_id", id);
    window.location.href = "chat.htm";
}
function process_admin_position(state){
  console.log(state);
  var localObj = JSON.parse(localStorage.getItem("userInfo"));
  var user = localObj.id;
  var check=localStorage.getItem('choose_state');
  if(check==1){
    $.post('destination.php',{
      type:'accept_admin_position',
      user:user,
      group:localStorage.getItem("accept_admin_position_group_id"),
      accept:state
    },function (data){
      console.log(data);
    });
  }else if(check==0){
    $.post('destination.php',{
      type:'accept_user_position',
      user:user,
      group:localStorage.getItem("accept_user_position_group_id"),
      accept:state
    },function (data){
      console.log(data);
    });
  }else if(check==2){
    var meeting_id=localStorage.getItem("meeting");
    $.post('destination.php',{
      type:'respond_to_meeting',
      me:user,
      meeting:meeting_id,
      state:state
    },function (data){
      console.log(data);
    });
  }else if(check==3){
    var payment_id=localStorage.getItem("Payment_Group_ID");
    $.post('destination.php',{
      type:'respond_to_payment_reminder',
      user:user,
      group:payment_id,
    },function (data){
      console.log(data);
    });
  }
  location.reload();
}
function set_modal(){
  $('body').append("<div class=\"modal fade\" id=\"myModal\" role=\"dialog\">" +
            "  <div class=\"modal-dialog\">" +
            "" +
            "    <!-- Modal content-->" +
            "    <div class=\"modal-content\">" +
            "      <div class=\"modal-header\">" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" +
            "        <h4 class=\"modal-title\">Info</h4>" +
            "      </div>" +
            "      <div class=\"modal-body\">" +
            "        <p id=\"popup\"></p>" +
            "      </div>" +
            "      <div class=\"modal-footer\">" +
            "        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onclick=\"process_admin_position(1);\">Accept</button>" +
            "        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onclick=\"process_admin_position(0);\">Reject</button>" +
            "      </div>" +
            "    </div>" +
            "" +
            "  </div>" +
            "</div>");
}
function admin_modal(name,id,group){
  //accept_admin_position
  localStorage.setItem('accept_admin_position',id);
  localStorage.setItem('accept_admin_position_group_id',group);
  localStorage.setItem('choose_state',1);
  $("#popup").text("You are being invited to be an admin user in the society "+name+" Do you accept");
  $('#myModal').modal('show');
}
function user_modal(name,id,group){
  //accept_admin_position
  localStorage.setItem('accept_user_position',id);
  localStorage.setItem('accept_user_position_group_id',group);
  localStorage.setItem('choose_state',0);
  $("#popup").text("You are being invited to be a user in the society "+name+" Do you accept");
  $('#myModal').modal('show');
}
function meeting_modal(meeting,name){
  localStorage.setItem('meeting',meeting);
  $("#popup").text("You are being invited the meeting for the society "+ name +". Will you be attending");
  $('#myModal').modal('show');
  localStorage.setItem('choose_state',2);
}
function payment_modal(Group,name){
  localStorage.setItem('Payment_Group_ID',Group);
  $("#popup").text("You are reminded about a payment from the society "+ name  +". Are you acknowledging the payment?");
  $('#myModal').modal('show');
  localStorage.setItem('choose_state',3);
}

function get_display() {
    var localObj = JSON.parse(localStorage.getItem("userInfo"));
    var user = localObj.id;
    $.post('destination.php', {
            type: "get_display_data",
            user: user,
        },
        function(data) {
            console.log(data);
            var info = JSON.parse(data);
            var messages = info.messages;
            var meetings = info.meetings;
            var admin_requests=info.admin_requests;
            var user_requests=info.user_requests;
            var payment_reminders=info.payment_reminder;
            $("#bar_menu").append(
                "<li class=\"divider\"></li>" +
                "<li><strong>Messages</strong></li>"
            );
            for (var i = 0; i < messages.length; i++) {
                $("#bar_menu").append(
                    "<li><a onclick=click_message(" + messages[i].belongs + ");>Message from" + messages[i].name + "</a></li>"
                );
            }
            $("#meetings").append(
                "<li><strong>Meetings<strong></li>"+
                "<li class=\"divider\"></li>"
            );
            for (var i = 0; i < meetings.length; i++) {
              var obj=meetings[i];
                $("#meetings").append(
                  '<li><a onclick="meeting_modal(\''+obj.id+ '\',\''+obj.name+ '\');">'  + meetings[i].name + " on " + meetings[i].date + " @ " + meetings[i].time +'</a></li>'

                );
            }

            $("#meetings").append(
                "<li class=\"divider\"></li>"+
                  "<li><strong>Admin Requests<strong></li>"+
                "<li class=\"divider\"></li>"
            );
            for (var i = 0; i < admin_requests.length; i++) {
              var obj=admin_requests[i];
              var name=obj.name;
              $("#meetings").append(
                  '<li><a onclick="admin_modal(\''+name+ '\',\''+obj.id+ '\',\''+obj.belongs+ '\');">' + admin_requests[i].name +'</a></li>'
              );
            }

            $("#meetings").append(
              "<li class=\"divider\"></li>"+
              "<li><strong>User Requests<strong></li>"+
              "<li class=\"divider\"></li>"
            );
            for (var i = 0; i < user_requests.length; i++) {
              var obj=user_requests[i];
              var name=obj.name;
                $("#meetings").append(
                    '<li><a onclick="user_modal(\''+name+ '\',\''+obj.id+ '\',\''+obj.belongs+ '\');">' + user_requests[i].name +'</a></li>'
                );
            }

            $("#meetings").append(
              "<li class=\"divider\"></li>"+
              "<li><strong>Payment Reminders<strong></li>"+
              "<li class=\"divider\"></li>"
            );
            for (var i = 0; i < payment_reminders.length; i++) {
               var obj=payment_reminders[i];
                $("#meetings").append(
                   '<li><a onclick="payment_modal(\''+obj.belongs+ '\',\''+obj.name+ '\');">' + obj.name +'</a></li>'
                );
            }
        }
    );
} //This will be sample manager

function newMemberModal(){
   $('#newMemberModal').modal('show');
}
function newMemberModalDismiss(){
   var name = $('#memberName').val();
   localStorage.setItem("memberSearchResults", name);
   window.location.href = 'memberResults.htm';
   $('#newMemberModal').modal('hide');
}
function memberSearch() {
    var name = localStorage.getItem("memberSearchResults");
    // alert(name);
    var type = 'search_user';
    $.post('destination.php', {
            name: name,
            type: type
        },
        function(data) {
 	console.log(data);
            /* alert(data);
             console.log(data);*/
            var obj = JSON.parse(data);
            if (obj.success == 0) {
                $('#noResultsModal').modal('show');
            } else {
                $("#memberResults tr").remove();
                for (var i = 1; i <= obj.data.length; i++) {
                    var user_name = obj.data[i - 1].name+" "+obj.data[i - 1].surname;
                    var user_id = obj.data[i - 1].id;
                    // console.log(group_data);
                    //This just inflates the data into the table
                    var user_ID = "I" + i;
                    $("#memberResults tbody").append(
                        "<tr id=" + user_id + ">" +
                        "<td id=" + i + " class='td_all' >" + i + "</td>" +
                        "<td>" + user_name + "</td>");
                    $('#memberResults').on('click', '#' + user_id, function() {
                        var localObj = JSON.parse(localStorage.getItem("userInfo"));
                        var added_by = localObj.id;
                        var user = this.cells[0].innerHTML;
			var group =  localStorage.getItem("group_id");
                        //alert('clicked');
                        var type = "add_user";
                        $.post('destination.php',
                            //data to be passed
                            {
                                user: user,
                                group: group,
				added_by:added_by,
                                type: type
                            },
                            //function called after success
                            function(data) {
				console.log(data);
                                $('#memberSearchModal').modal('show');
                            }
                        );
                    });
                }
            }
        }	
    );
}

function remindModal(){
   $('#reminderModal').modal('show');
}
function dismissModal(){
   $('#reminderModal').modal('hide');
}
function getMeetings(){
  var group = localStorage.getItem("group_id");
  var type = 'get_meetings';
  $.post('destination.php',{
      group:group,
      type:type
  },
  function(data){
    var obj = JSON.parse(data);
     for (var i = 0; i < obj.data.length; i++) {
         $("#remindMeetTable tbody").append(
             "<tr>" +
             "<td>" + obj.data[i].message + "</td>" +
             "<td>" + obj.data[i].date + "</td>" +
             "<td>" + obj.data[i].time + "</td>" +
             "<td>" + obj.data[i].venue + "</td>" +
             "<td> <label><input type=\"radio\" name=\"dynradio\" id="+ obj.data[i].id + "></label></td>" +
             "</tr>"
         );
     }
  });
}
function remindMeeting(){
  var meeting = document.querySelector('input[name="dynradio"]:checked').id;
  var type = 'remind_meeting';
  $.post('destination.php',{
    meeting:meeting,
    type:type
  },
  function(data){
    console.log(data);
  });
}
//Payment Reminders
function getDebtors(){
  var group = localStorage.getItem("group_id");
  var type = 'get_debtors';
  $.post('destination.php',{
      group:group,
      type:type
  },
  function(data){
    console.log(data);
    var obj = JSON.parse(data);
     for (var i = 0; i < obj.data.length; i++) {
         $("#remindPayTable tbody").append(
             "<tr>" +
             "<td>" + obj.data[i].name +" "+ obj.data[i].surname + "</td>" +
             "<td>" + obj.data[i].amount + "</td>" +
             "<td>" + obj.data[i].payback_date + "</td>" +
             "<td> <label><input type=\"radio\" name=\"nradio\" id="+ obj.data[i].id + "></label></td>" +
             "</tr>"
         );
     }
  });
}
function remindPayment(){
  var user = document.querySelector('input[name="nradio"]:checked').id;
  var group = localStorage.getItem("group_id");
  var message = $("#payReminder").val();
  var type = 'remind_payment';
  $.post('destination.php',{
    user:user,
    group:group,
    message:message,
    type:type
  },
  function(data){
    console.log(data);
  });
}
//JQuery code to make tables responsive
$('.table tbody tr').click(function(event) {
  if (event.target.type != 'radio') {
    $(':radio', this).trigger('click');
  }
});

function go_back() {
    console.log("Going back");
    window.history.back();
}
