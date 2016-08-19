<?php
//Event type, 1==club and 0==user event
  $type=$_REQUEST["type"];
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
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_upcoming_events($id);
      echo json_encode($info);
      break;
    case 'get_ongoing_events':
      $id=$_REQUEST["id"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->get_ongoing_events($id);
      echo json_encode($info);
      break;
    case 'get_user_events_attending':
      # code...
      break;
    case 'get_user_events_hosting':
      # code...
      break;
    case 'get_club_info':
      # code...
      break;
    case 'get_user_info':
      # code...
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
      $latlong=$_REQUEST["latlong"];
      $me=$_REQUEST["me"];
      $djs=$_REQUEST["djs"];
      $specials=$_REQUEST["specials"];
      $gen_fee=$_REQUEST["gen_fee"];
      $vip_fee=$_REQUEST["vip_fee"];
      $start_time=$_REQUEST["start_time"];
      $end_time=$_REQUEST["end_time"];
      $ext=$_REQUEST["ext"];
      $pic=$_REQUEST["pic"];
      include_once "EventManagement.php";
      $events=new Events();
      $info=$events->user_create_event($name,$address,$latlong,$me,$djs,$specials,$gen_fee,$vip_fee,$start_time,$end_time,$ext,$pic);
      echo json_encode($info);
      break;
    default:
      # code...
      break;
  }
?>
