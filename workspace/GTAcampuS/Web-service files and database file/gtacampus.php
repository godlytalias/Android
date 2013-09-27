<?php $server="localhost";
$sqlid="ENTER YOUR SQL ID HERE";
$sqlpass="ENTER YOUR SQL PASSWORD HERE";
$dbase="ENTER YOUR DATABASE NAME HERE";
$connection=mysql_connect($server,$sqlid,$sqlpass);
if(!$connection)
{
die("database failed".mysql_error());}
$dbselect=mysql_select_db($dbase,$connection);
if(!dbselect)
{
die("database failed".mysql_error());}
if(isset($_POST['sender']) && isset($_POST['message']))
{
$result=mysql_query("INSERT INTO gtacampus (sender,message) VALUES ('$_POST[sender]','$_POST[message]')");
if($result)
echo "Message sent successfully";
else
echo "Message sending failed";
}
else
{echo "SENDING FAILED".mysql_error();}

mysql_close($connection);
?>
