<?php $server="localhost";
$sqlid="ENTER YOUR SQL ID HERE";
$sqlpass="ENTER YOUR SQL PASSWORD HERE";
$dbase="ENTER YOUR DATABASE NAME HERE";
$connection=mysql_connect($server,$sqlid,$sqlpass);
if(!$connection)
{
die("database failed".mysql_error());}
$dbselect=mysql_select_db($dbase,$connection);
if(!$dbselect)
{
die("database failed".mysql_error());}

$msgs = mysql_query("SELECT sender,id FROM gtacampus ORDER BY id DESC");
$result="";

while($msg = mysql_fetch_array($msgs)){
$result = $result. $msg[1] . " . " . $msg[0] . "\r\n";
}
 /*print(json_encode($result));*/
 echo $result;
mysql_close($connection);
?>
