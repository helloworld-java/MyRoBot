package mybot;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {
    static String url="jdbc:mysql://localhost:3306/maoche?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static String user="maoche";
    static String password="xiaojiang110";
    static String SQL = "SELECT * FROM ";// 数据库操作
    ResultSet rs= null;
    static Statement stat = null;
    static Connection conn = null;
    static PreparedStatement pStemt = null;
    static String msg = "数据库连接失败！请联系管理员！";
    public Data(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(url,user,password);
            if (!conn.isClosed()){System.out.println("成功连接数据库！");}
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public List<String> mysql(String tableName) {
        List<String> columnNames = new ArrayList<>();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0;i<size;i++){
                columnNames.add(rsmd.getColumnName(i+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return columnNames;
    }

    /**
     * @param "签到存入玩家信息"
     * */
    public void addPlayer_info(String Name,String QQ,String Balance,String Time,String UID,String Number,String Favor){
        String sql = "insert into player_info(Name,QQ,Balance,Time,UID,Number,Favor) values(?,?,?,?,?,?,?)";
        try{
            pStemt = conn.prepareStatement(sql);
            pStemt.setString(1,Name);
            pStemt.setString(2,QQ);
            pStemt.setString(3,Balance);
            pStemt.setString(4,Time);
            pStemt.setString(5,UID);
            pStemt.setString(6,Number);
            pStemt.setString(7,Favor);
            pStemt.executeUpdate();
           // System.out.println("签到插入数据成功！");
        }catch (Exception e){
            System.out.println("新玩家签到未知错误!");
        }
    }

    /**
     * @param签到玩家信息更改
     * */
    public void upPlayer_info(String Name,String QQ,String Balance,String Time,String UID,String Number,String Favor){
        String sql = "update player_info set Name=?,QQ=?,Balance=?,Time=?,UID=?,Number=?,Favor=? where QQ='"+QQ+"'";
        try{
            pStemt = conn.prepareStatement(sql);
            pStemt.setString(1,Name);
            pStemt.setString(2,QQ);
            pStemt.setString(3,Balance);
            pStemt.setString(4,Time);
            pStemt.setString(5,UID);
            pStemt.setString(6,Number);
            pStemt.setString(7,Favor);
            pStemt.executeLargeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查询
     *
     * **/
    public List<String> Player_info(String QQ){
        String sql="select * from player_info where QQ='"+QQ+"'";
        List<String> info = new ArrayList<>();
        try{
            Statement stmt= this.conn.createStatement();
            rs= stmt.executeQuery(sql);
            List<String> list = new ArrayList<>(this.mysql("player_info"));
            while(rs.next()) {
                for (int i = 0; i < list.size(); i++) {
                    String friend_1 = rs.getString(list.get(i));
                    info.add(friend_1);
                    System.out.println(friend_1);
                }
                return info;
            }
        }catch(Exception e){
            e.printStackTrace();
            //System.out.println("好友列表查询出现错误");
        }
        return null;
    }

    /**
     * @param签到查询QQ号
     * */
    public String QQ(String QQ){
        String sql="select * from player_info where QQ='"+QQ+"'";

        try{
            Statement stmt= conn.createStatement();
             rs= stmt.executeQuery(sql);
            while(rs.next()){
                String QQA=rs.getString("QQ");
                System.out.println("查询数据库QQ号:"+QQA);
                return QQA;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param命运游戏查询金币余额
     * @return*/
    public String selBalance(String QQ){
        String sql="select Balance from player_info where QQ='"+QQ+"'";

        try{
            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            while(rs.next()){
                String Balance=rs.getString("Balance");
                //System.out.println("命运查询金币余额！");
                return Balance;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * @param命运玩家金币更改
     * */
    public void upBalance(String QQ,String Balance){
        String sql = "update player_info set Balance=? where QQ='"+QQ+"'";
        try{
            pStemt = conn.prepareStatement(sql);
            pStemt.setString(1,Balance);
            pStemt.executeLargeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * @param对话查询
     */
    public List<String> selMsg(String Msg){
        String sql="select * from message where message='"+Msg+"'";
        List<String> msg = new ArrayList<>();
        try{
            Statement stmt= this.conn.createStatement();
            rs= stmt.executeQuery(sql);
            List<String> list = new ArrayList<>(this.mysql("message"));
            while(rs.next()) {
                for (int i = 0; i < list.size(); i++) {
                    String message = rs.getString(list.get(i));
                    if (message != null) {
                        msg.add(message);
                        //System.out.println(message);
                    }
                }
                if (msg.size()>=1) {
                    return msg;
                }else {
                    System.out.println("未查询到相关记录。");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("对话查询出现错误："+Msg);
        }
        return null;
    }

    /**
     * @param聊天记录
     *      查询数据库记录是否存在一条聊天信息
     *      如果存在则在记录后面添加一个字段记录
     *      如果没有则新增一条记录
     * */
    public void addMsg(String Msg,String Msg_1){
        long date = System.currentTimeMillis();
        //获取message表所有字段
        List<String> list = new ArrayList<>(this.mysql("message"));
        //增加记录
        String addSql = "insert into message(message,message_1) values(?,?)";
        //增加一个字段
        String sql = "ALTER TABLE message add message_"+list.size()+" CHAR(255) DEFAULT NULL;";

        try {
            String msg = this.selMessage(Msg);
            //如果数据库没有这条问题记录则增加一条记录
            if (msg==null){
                pStemt = conn.prepareStatement(addSql);
                pStemt.setString(1,Msg);
                pStemt.setString(2,Msg_1);
                pStemt.executeLargeUpdate();
                pStemt.close();
            }else {
                //获取所有回答
                List<String> list_size = new ArrayList<>(this.selMessage_1(Msg));
                //获取不为空的有效回答
                List<String> list_1 = new ArrayList<>();
                for (int i = 0; i < list_size.size(); i++) {
                    if (list_size.get(i) != null) {
                        list_1.add(list_size.get(i));
                        //System.out.println(list_size.get(i));
                    }
                }
                //System.out.println("list\n"+list.size()+"list_size"+list_size.size());
                String message = "message_" + list_1.size();
                //修改记录
                String upSql = "update message set " + message + "=? where message='" + Msg + "'";
                for (int i = 0; i < list_size.size(); i++) {
                    if (!list_size.get(i).equals(msg)) {
                        if (list_1.size() < list.size()) {
                            pStemt = conn.prepareStatement(upSql);
                            pStemt.setString(1, Msg_1);
                            pStemt.executeLargeUpdate();
                        }
                    }
                    //System.out.println(list_size.get(i));
                }
                for (int i = 0; i < list_size.size(); i++) {
                    if (!list_size.get(i).equals(msg)&&!list_size.get(i).equals(Msg_1)) {
                        if (list_1.size() == list.size()) {
                            stat = conn.createStatement();
                            stat.executeUpdate(sql);
                            pStemt = conn.prepareStatement(upSql);
                            pStemt.setString(1, Msg_1);
                            pStemt.executeLargeUpdate();
                        }}}
                long date_1 = System.currentTimeMillis();
               // System.out.println("本次执行时间：" + (date_1 - date) + "ms");
            } }catch (Exception e){e.printStackTrace();}
    }
    /**
     * @param查询一个问题是否存在
     * */
    public  String selMessage(String Msg){
        String selSql = "select message from message where message='"+Msg+"'";
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(selSql);
            while (rs.next()){
                String msg = rs.getString("message");
                return msg;
            }
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    /**
     * @param查询所有回答
     * */
    public List<String> selMessage_1(String Msg){
        String sql="select * from message where message='"+Msg+"'";
        List<String> msg = new ArrayList<>();
        try{
            Statement stmt= this.conn.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            List<String> list = new ArrayList<>(this.mysql("message"));
            while(rs.next()) {
                for (int i = 1; i < list.size(); i++) {
                    String message = rs.getString(list.get(i));
                    if (message != null){
                    msg.add(message);
                    //System.out.println(message);
                        }
                }
                return msg;
            }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("查询所有回答出现错误："+Msg);
        }
        return null;
    }

    /**
     * @param查询金币排行
     * */
    public List<Integer> selBalance(){
        String sql = "select Balance from player_info";
        try{
            List<Integer> list = new ArrayList<>();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String balance = rs.getString("Balance");
                list.add(Integer.parseInt(balance));
            }
            Collections.sort(list);
            return list;
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    /***
     * @param通过金币数量查询QQ号
     */
    public String balance_QQ(int balance){
        String sql = "select Name from player_info where Balance='"+balance+"'";
        try{
            //System.out.println(balance);
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String qq= rs.getString("Name");
                //System.out.println(qq);
                return qq;

            }
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    /**
     * 删除对话记录
     * */
    public void delMessage_1(String Msg){
        String sql="delete from message where message='"+Msg+"'";
        try{
            Statement stmt= this.conn.createStatement();
            stmt.executeLargeUpdate(sql);
        }catch(Exception e){
            System.out.println("对话删除出现错误："+Msg);
        }
    }
}

