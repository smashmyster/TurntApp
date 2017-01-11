<?php
  /**
   *
   */
  class GCM
  {
    function send_invite($tokens,$message){
      $url='https://fcm.googleapis.com/fcm/send';
      $fields=array(
          'registration_ids'=>$tokens,
          'data'=>$message//array("message" => " FCM PUSH NOTIFICATION TEST MESSAGE","type"=>'order')
      );
      $hearders=array('Authorization:key=AIzaSyBSpJXB363kGmkAI2MYdmj5ImGq4b2TrJk',
      'Content-Type:application/json');
      $ch = curl_init();
      curl_setopt($ch, CURLOPT_URL, $url);
      curl_setopt($ch, CURLOPT_POST, true);
      curl_setopt($ch, CURLOPT_HTTPHEADER, $hearders);
      curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
      curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
      curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
      curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
      $result = curl_exec($ch);
      if ($result === FALSE) {
          die('Curl failed: ' . curl_error($ch));
      }
      curl_close($ch);
      echo $result;
    }
  }

?>
