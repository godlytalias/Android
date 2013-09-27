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
if(isset($_POST['id']))
{
$id=$_POST['id'];
$result=mysql_query("SELECT message,date FROM gtacampus WHERE id=$id");
if($result)
{
$message = mysql_fetch_array($result);
$result = $message[0]. " \n\nSent on :\n " .$message[1];
echo $result;
}
else
echo "Can't read message from server";
}
else
{echo "WHAT TO READ?!";}

mysql_close($connection);
?>
