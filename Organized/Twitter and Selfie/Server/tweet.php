<?php

require_once ('twitterAPI.php');

function tweet ($conn, $message) {
	if (!$conn) return;

	$conn->request (
		'POST',
		'https://api.twitter.com/1.1/statuses/update.json',
		array (
			'status' => $message
		),
		true,
		true
	);
}

function tweet_photo ($conn, $message, $photo) {
	if (!$conn) {
	   echo "Conn is null\n";
	   return;
	}

	$conn->request (
		'POST',
		'https://api.twitter.com/1.1/statuses/update_with_media.json',
		array (
			'media[]' => "@{$photo};type=image/jpg;filename={$photo}",
			'status'  => $message
		),
		true, 
		true
	);
}

$connection = new tmhOAuth(array (

	'user_token' => "2845164320-kStYbmCCNIDCJayEjuateXlrv6cl2UHpayyb0xU",
	'user_secret' => "TZmyy82NO48oQ4fii7BDmI0qgQhW6khTRlE8Kqiw9t1ha",
	'consumer_key' => "zw1p29O8r3wuDAqWpGssk2xew",
	'consumer_secret' => "lnZImZhBZObEmP6GE471QkTFy8kUKlD9rywuCSjgwEOR9TZe2C"

));


if (isset ($_FILES ['file']) && isset ($_POST ['tweet'])) {
	move_uploaded_file($_FILES ['file']['tmp_name'], $_FILES ['file']['name']);
	tweet_photo ($connection, $_POST ['tweet'], $_FILES ['file']['name']);
}

else if ($_GET ['tweet']) {
	tweet ($connection, $_GET ['tweet']);
}
?>