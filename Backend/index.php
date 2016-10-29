<?php
//Event type, 1==club and 0==user event
  $type="";
  if(isset($_REQUEST["type"])){
    $type=$_REQUEST["type"];
  }
  switch ($type) {
    case 'sign_up':
      $user=$_REQUEST["user"];
      $pass=sha1($_REQUEST["pass"]);
      $email=$_REQUEST["email"];
      $name=$_REQUEST["name"];
      $surname=$_REQUEST["surname"];
      $bday=$_REQUEST["bday"];
      $phone=$_REQUEST["phone"];
      $image=$_REQUEST["image"];
      $ext=$_REQUEST["ext"];
      $regid=$_REQUEST["regid"];
      $gender=$_REQUEST["gender"];
      include_once 'ProfileActions.php';
      $useractions=new UserActions();
      $info=$useractions->sign_up($user,$pass,$email,$name,$surname,$bday,$phone,$image,$ext,$regid,$gender);
      echo json_encode($info);
      break;
    case 'login':
      $user=$_REQUEST["user"];
      $pass=sha1($_REQUEST["pass"]);
      include_once 'ProfileActions.php';
      $useractions=new UserActions();
      $info=$useractions->login($user,$pass);
      echo json_encode($info);
      break;
    case 'get_upcoming_events':
      $id=$_REQUEST["id"];
      $user=$_REQUEST["user"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_upcoming_events($id,$user);
      echo json_encode($info);
      break;
    case 'attending':
      $id=$_REQUEST["id"];
      $event=$_REQUEST["event"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->attend_event($id,$event);
      echo json_encode($info);
      break;
      case 'unattending':
        $id=$_REQUEST["id"];
        $event=$_REQUEST["event"];
        include_once "EventManagement.php";
        $events=new Events();
        $info=$events->unattend_event($id,$event);
        echo json_encode($info);
        break;
    case 'get_ongoing_events':
      $id=$_REQUEST["id"];
      $user=$_REQUEST["user"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_ongoing_events($id,$user);
      echo json_encode($info);
      break;
    case 'get_user_events_attending':
        $me=$_REQUEST["me"];
        $user=$_REQUEST["user"];
        include_once "EventManagement.php";
        $events=new Events();
        $info=$events->get_user_events_attending($user,$me);
        echo json_encode($info);
      break;
    case 'get_my_events':
      $me=$_REQUEST["me"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_user_events_attending_allowed($me);
    echo json_encode($info);
      break;
    case 'get_user_events_hosting':
      $me=$_REQUEST["me"];
      $user=$_REQUEST["user"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_user_events_attending($user,$me);
      echo json_encode($info);
      break;
    case 'get_club_info':

      break;
    case 'get_user_info':
      $me=$_REQUEST["me"];
      $person=$_REQUEST["user"];
      include_once 'InfoExchange.php';
      $get=new InfoExchange();
      $info=$get->get_user_info($me,$person);
      echo json_encode($info);
      break;
    case 'invite_user':
      $me=$_REQUEST["me"];
      $user=$_REQUEST["user"];
      $event=$_REQUEST["event"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->invite_user($me,$user,$event);
      echo json_encode($info);
      break;
    case 'respond_to_invite':
      $requst_id=$_REQUEST["requst_id"];
      $accept=$_REQUEST["accept"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->respond_to_invite($requst_id,$accept);
      echo json_encode($info);
      break;
    case 'get_events_users_attending':
      $event=$_REQUEST["event"];
      $me=$_REQUEST["me"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_events_users_attending($event);
      echo json_encode($info);
      break;
    case 'get_invitable_list':
        $event=$_REQUEST["event"];
        $me=$_REQUEST["me"];
        include_once "EventManagement.php";
        $events=new Events();
        $info=$events->get_invitable_list($event,$me);
        echo json_encode($info);
      break;
    case 'create_user_event':
      $name=$_REQUEST["name"];
      $address=$_REQUEST["address"];
      $me=$_REQUEST["me"];
      $djs=$_REQUEST["djs"];
      $specials=$_REQUEST["specials"];
      $gen_fee=$_REQUEST["gen_fee"];
      $vip_fee=$_REQUEST["vip_fee"];
      $start_time=$_REQUEST["start_time"];
      $end_time=$_REQUEST["end_time"];
      $ext=$_REQUEST["ext"];
      $pic=$_REQUEST["thumb"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->user_create_event($name,$address,$me,$djs,$specials,$gen_fee,$vip_fee,$start_time,$end_time,$ext,$pic);
      echo json_encode($info);
      break;
    case 'get_my_followers':
        include_once 'InfoExchange.php';
        $exchange=new InfoExchange();
        $me=$_REQUEST["id"];
        $info=$exchange->get_my_followers($me);
        echo json_encode($info);
        break;
    case 'search_user':
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $text=$_REQUEST["search"];
      $me=$_REQUEST["me"];
      $info=$exchange->search($text,$me);
      echo json_encode($info);
      break;
    case 'search_event':
      include_once 'InfoExchange.php';
      $group_run=new InfoExchange();
      $name=$_REQUEST["search"];
      $info=$group_run->search_event($name);
      echo json_encode($info);
      break;
    case 'follow_user':
      include_once 'ProfileActions.php';
      $useractions=new UserActions();
      $me=$_REQUEST["me"];
      $user=$_REQUEST["user"];
      $info=$useractions->follow_user($me,$user);
      echo json_encode($info);
      break;
      case 'unfollow_user':
      include_once 'ProfileActions.php';
      $useractions=new UserActions();
      $me=$_REQUEST["me"];
      $user=$_REQUEST["user"];
      $info=$useractions->unfollow_user($me,$user);
      echo json_encode($info);
      break;
    case 'test_gcm':
      $token=array();
      array_push($token,'fXtJe_KL9Ws:APA91bHzluZ4vcZKXJH8036GOnnZ75Yv_hEC35xCl8QouB0YqMvgGDx3p8NS0UdjEUQbJvcSsCXnj7lv_QlFjtQ9NJbltg26RaQVQwMlPkbk8DDICKfnW1bjKcqreF2khAhfSXuNR0gZ');
      $message["message"]=1;
      include_once 'SendGCM.php';
      $send=new GCM();
      $send->send_invite($token,$message);

      break;
    default:
      echo json_encode(array('success'=>-1,'message'=>'Unknown Request'));
      break;
  }
?>
