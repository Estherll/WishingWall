$(document).ready(function() {
    idContentchange();
    indexContentchange();
    clickHeart();
});
// 用户界面心愿、评论切换
function idContentchange(){
    var $idContent_li =$(".id_tab_menu ul li");
    $idContent_li.click(function(){
        $(this).addClass("id_selected")
            .siblings().removeClass("id_selected");
        var index = $idContent_li.index(this);
        $(".id_tab_box>div")
            .eq(index).show()
            .siblings().hide();
    })
}


// 许愿主页热门许愿、本周最新、热门话题
function indexContentchange(){
    var $indexContent_li =$(".index_tab_menu ul li");
    $indexContent_li.click(function(){
        $(this).addClass("index_selected")
            .siblings().removeClass("index_selected");
        var index = $indexContent_li.index(this);
        $(".index_tab_box>div")
            .eq(index).show()
            .siblings().hide();
    })
}


//点赞
function clickHeart(){
    $(".love>img").live("click",function (){
        var imgsrc=$(this).attr('src');
        var mid=$(this).attr('mid');
        var loveNum=parseInt($(this).siblings().eq(0).text());
        if(imgsrc=='../img/loved.png'){
            $(this).attr('src','../img/love.png');
            if(loveNum>0){
                $(this).siblings().eq(0).text((loveNum-1));
            }
        }else{
            $(this).attr('src','../img/loved.png');
            $(this).siblings().eq(0).text((loveNum+1));                             
        }
        
         $.ajax({
             url: '../enter/homePage_like.action?targetMid='+1,     //要访问的service路径
             type: 'get',        //访问实现的方式:get 或 post
             async: true,      //是否异步
             cache:false ,      //是否缓存
             success:function(result){    //请求成功后执行
             }
        });
    });
}

$(".message").each(function(){
  var maxwidth=63;
  if($(this).text().length > maxwidth){
    $(this).text($(this).text().substring(0,maxwidth));
    $(this).html($(this).html()+'...');
  }
});
