var botname = "super";
var botimgPath="images/photo.jpg";
var userName = "client"
var userimgPath="images/photo.jpg";

function setUserImage(path)
{
 userimgPath=path;
}

/*
function setBotName(name)
{
 botname = name; 
}
*/

function setBotImage(path)
{
 botimgPath=path;
}

function setUserName(name)
{
 userName = name; 
}

function toBottom()
{
 window.scrollTo(0, document.body.scrollHeight);
}

function addTextC(message){

 var x = document.createElement('div');
    x.setAttribute('class','client_content_box');
   
    x.innerHTML= ' <table class="table_content">'+
       '<tr>'+
          '<td clas="image_cell" width="20%" >'+
             //'<div class="client_content_image">'+
                '<img class="image" src='+userimgPath+'>'+
             //'</div>'+
          '</td>'+
          '<td width="80%" >'+
             '<div class="client_content_name">'+
                     '<b>'+userName+'</b>'+
             '</div>'+
          '</td>'+
       '</tr>'+
       '<tr>'+
          '<td halign="left" valign="top" colspan="2">'+
             '<div class="content_data">'+
             message+
             '</div>'+
          '<td>'+
       '</tr>'+
    '</table> ';
    document.getElementById('Communication').appendChild(x);
    
    toBottom();
}

function addTextS(message)
{

  var x = document.createElement('div');
    x.setAttribute('class','server_content_box');
   
    x.innerHTML= ' <table class="table_content">'+
       '<tr>'+          
          '<td width="80%" >'+
             '<div class="server_content_name">'+
                     '<b>'+ botname +'</b>'+
             '</div>'+
          '</td>'+
           '<td clas="image_cell" width="20%" >'+
             //'<div class="server_content_image">'+
                '<img class="image" src='+botimgPath+'>'+
             //'</div>'+
          '</td>'+ 
       '</tr>'+
       '<tr>'+
          '<td halign="left" valign="top" colspan="2">'+
             '<div class="content_data">'+
             message+
             '</div>'+
          '<td>'+
       '</tr>'+
    '</table> ';
    document.getElementById('Communication').appendChild(x);

   toBottom();
}
