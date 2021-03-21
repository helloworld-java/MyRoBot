package mybot;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import love.forte.catcode.CatCodeUtil;
import love.forte.catcode.CodeBuilder;
import love.forte.catcode.Neko;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class diary {
    Data data = new Data();
    //设置APPID/AK/SK
    public static final String APP_ID = "23657345";
    public static final String API_KEY = "5KROuA7RRiBPGfVsWTsr0mvB";
    public static final String SECRET_KEY = "oonnZGNbEvBoZNNtlDZ9PXe4SUWLL4qP";
    final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;

    /**
     * 猫车文字内容识别
     * */
    public String McText(String Text) {
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 参数为本地图片路径
        String text = Text;
        JSONObject response = client.textCensorUserDefined(text);
        //System.out.println(response.toString());
        String msg = (String) response.get("conclusion");
        return msg;
    }

    /**
     * 猫车色图识别
     * */
    public String McImg(String Url){
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String url = Url;
        JSONObject response = client.imageCensorUserDefined(url, EImgType.URL, null);
        String msg = (String) response.get("conclusion");
        //System.out.println(response.get("conclusion"));
        return msg;
    }
//获取@人的QQ号
    public String atCode(String msg){
        //System.out.println(msg+"\n");
        String[] Msg = msg.split("#");
       // System.out.println(Msg.length);
        //System.out.println("0："+Msg[0]+"\n"+"1："+Msg[1]+"\n"+"2："+Msg[2]+"\n"+"3："+Msg[3]+"\n"+"4："+Msg[4]+"\n");
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(Msg[1]);
        String qq = m.replaceAll("").trim();
       // System.out.println(qq);
        String q = null;
        if (Msg.length == 3){
            return q=qq+"#"+Msg[2];
        }else if (Msg.length == 5){
            return q=qq+"#"+Msg[4];
        }else if (Msg.length == 2)
        {
            return  q = qq;
        }
        return q;
    }

    //命运游戏金币增加
    public String add_balance(String QQ,long ba){
        long balanc = Long.parseLong(data.selBalance(QQ));//获取金币余额
        String balan = String.valueOf(ba + balanc);
        data.upBalance(QQ,balan);//更新金币数据
        return data.selBalance(QQ);
    }

    //命运游戏金币减少
    public String subtract_balance(String QQ,long ba){
        long balanc = Long.parseLong(data.selBalance(QQ));//获取金币余额
        String balan = String.valueOf(balanc - ba);
        data.upBalance(QQ,balan);//更新金币数据
        return data.selBalance(QQ);
    }

    //敏感词
    public String shield(String msg_1){
        String msg = msg_1;
        String[] shield = {
                "充值","命运","菜单", "查询", "签到", "退群", "日记", "转账", "妈妈", "国", "国家", "中国","习大大","周恩来","总理","主席","毛泽东","毛主席","共产党","打飞机","草",
                "杀","日","国务院","共产主义","革命","造反","起义","鸡吧","鸡","约炮","炮",
                "枪","手枪","抢劫","强奸","轮奸","乱奸","乱伦","亂倫","伦理","炸","骚","做爱","日批","援交","法轮功",
                "邪教","血","肛交","肛","屁","屁眼","阴道","子宫","阴","淫","黑帮","社会","黑社会","政府","少妇","贪官","迷药",
                "迷晕","死","妓女","妓院","毒","冰毒","两腿之间","两腿","插","技师","激情","女人","换妻","成人","避孕套",
                "黄色","裸体","自慰","乳房","奶水","乳汁","爱液","精子","精液","精子","高潮","潮吹","sm","捆绑","绑架","催情","人流",
                "药","口交","口我","后入","雞巴","警察","习进平","习晋平","内射","代孕","人民","穴","肉便器",
                "裸体","肉棒","肉","洞","乳交","砍","性爱","性","乳","胸","公安","公安局","烧","颜射","射精","泄了","深喉","陰",
                "淫","法轮","兽交","祖宗","全家","批","同性恋","下面","操你","日本","日本人",
                "鬼子","黄","图","涩","728774582","1878972643","抚慰","想要","嗯","啊","~",",主人",
                "快一点","好棒","好给力","想要","那里","菊花","欲望","傻逼","狗","儿子",
                "爸爸","服侍","妈","轻点","？","?","……","弄疼","我了","哥哥","…","RBQ","rbq","ghs","GHS",
                "麻批","爹","娘","操","淦","image","妈","*","射","痛","太","大","爱女人","爱液","按摩棒","乳","骚","逼","比","骚女","水","色","穴","情","爽","奸","欲","精","妇","母","熟","臀","死","袜","丝","舔","阴","调教","偷","内","xue","写真","性","饥渴","虐","口","阳","核","赞","红包","邀人","主人","棍子","记录",".h","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",
        };
        for(int i = 0; i<shield.length; i++){
            String shield_1 = shield[i];
            msg = msg.replace(shield_1,"");
        }
        return msg;
    }

    //玩家最终金币计算
    public int shu_sum(int balance,int sum_balance,int QQ_balance) {
        int sum = balance + sum_balance+QQ_balance;
        return sum;
    }


    /**
     * 群名检测
     *
     * @return*/
    public boolean groupName(String groupName){
        String msg = groupName;
        String m="";
        String[] shield = {
                "充值","命运","菜单", "查询", "签到", "退群", "日记", "转账", "妈妈", "国", "国家", "中国","习大大","周恩来","总理","主席","毛泽东","毛主席","共产党","打飞机","草",
                "杀","日","国务院","共产主义","革命","造反","起义","鸡吧","鸡","约炮","炮",
                "枪","手枪","抢劫","强奸","轮奸","乱奸","乱伦","亂倫","伦理","炸","骚","做爱","日批","援交","法轮功",
                "邪教","血","肛交","肛","屁","屁眼","阴道","子宫","阴","淫","黑帮","社会","黑社会","政府","少妇","贪官","迷药",
                "迷晕","死","妓女","妓院","毒","冰毒","两腿之间","两腿","插","技师","激情","女人","换妻","成人","避孕套",
                "黄色","裸体","自慰","乳房","奶水","乳汁","爱液","精子","精液","精子","高潮","潮吹","sm","捆绑","绑架","催情","人流",
                "药","口交","口我","后入","雞巴","警察","习进平","习晋平","内射","代孕","人民","穴","肉便器",
                "裸体","肉棒","肉","洞","乳交","砍","性爱","性","乳","胸","公安","公安局","烧","颜射","射精","泄了","深喉","陰",
                "淫","法轮","兽交","祖宗","全家","批","同性恋","下面","操你","日本","日本人",
                "鬼子","黄","图","涩","728774582","1878972643","抚慰","想要","嗯","啊","~",",主人",
                "快一点","好棒","好给力","想要","那里","菊花","欲望","傻逼","狗","儿子",
                "爸爸","服侍","妈","轻点","？","?","……","弄疼","我了","哥哥","…","RBQ","rbq","ghs","GHS",
                "麻批","爹","娘","操","淦","image","妈","*","射","痛","太","大","爱女人","爱液","按摩棒","乳","骚","逼","比","骚女","水","色","穴","情","爽","奸","欲","精","妇","母","熟","臀","死","袜","丝","舔","阴","调教","偷","内","xue","写真","性","饥渴","虐","口","阳","核","赞","红包","邀人","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",
        };
        for(int i = 0; i<shield.length; i++){
            String shield_1 = shield[i];
            m = msg.replace(shield_1,"");
        }
        if (!m.equals(msg)){
            return true;
        }else {return false;}
    }

    /**
     * 获取头像
     */
    public String Avatar(String image){
        String images = catUtil.toCat("image", false,
                "url="+image);
        return images;
    }
    public Neko share(String coverUrl,String title,String content,String url){
        final CodeBuilder<Neko> a = catUtil.getNekoBuilder("share",true);
        final CodeBuilder<String> stringCodeBuilder = catUtil.getStringCodeBuilder("share",false);
        Neko share = a
                .key("coverUrl").value(coverUrl)
                .key("title").value(title)
                .key("content").value(content)
                .key("url").value(url)
                .build();
        //System.out.println(share);
        return share;
    }
}
