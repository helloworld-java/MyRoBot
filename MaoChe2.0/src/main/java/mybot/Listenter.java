package mybot;

import WordFilter.WordContext;
import WordFilter.WordFilter;
import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountContainer;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupCodeContainer;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.api.message.results.BanList;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Beans
public class Listenter {
    Data data = new Data();
    diary dy = new diary();
    final catcode.CatCodeUtil catUtil = CatCodeUtil.INSTANCE;

    /**
     * 词库上下文环境
     */
    private WordContext context = new WordContext();
    private WordFilter filter = new WordFilter(context);

    /**
     * 注入一个消息构建器工厂
     */
    @Depend
    private MessageContentBuilderFactory messageContentBuilderFactory;
    /**
     * 监听私聊消息并复读。
     * */
    @OnPrivate
    @Filter(value = "nih")
    public void privatMsg(PrivateMsg Msg, MsgSender sender){
        sender.SENDER.sendPrivateMsg(Msg, Msg.getMsg()) ;
        Map <String,Object> a = new HashMap();
        a.put("title","猫车");
        a.put("content","猫车妙妙屋");
        a.put("url","https://www.baidu.com");
        a.put("coverUrl",Msg.getAccountInfo().getAccountAvatar());
        String share = catUtil.toCat("share",a);
        sender.SENDER.sendPrivateMsg(Msg, share);
        System.out.println(share);
    }

    /**
     * 猫车机器人菜单
     * 及对话回复
     * */
    @OnGroup
    //@Filter(value = "hhh")
    public void GroupMsg(GroupMsg groupMsg, MsgSender sender){
        try {
            if (groupMsg.getMsg().length() >= 400) {
                sender.SETTER.setMsgRecall(groupMsg.getFlag());
            } else {
                switch (groupMsg.getMsg()) {
                    case "菜单":
                        String menu = "你如果有需要帮助，而我可以帮得上忙的话，就用下面的指令吧~\n";
                        String msg = ".h1：认识一下猫车吧~" +
                                "\n.h2：查询自己的金币信息" +
                                "\n.h3：一个小小的金币游戏" +
                                "\n.h4：你是否觉得自己的金币不够用了呢？" +
                                "\n.h5：如果你有多余的金币不妨分享一些给猫车吧~" +
                                "\n.h6：猫车也想学习新东西哦。！" +
                                "\n.h7：你可以查看猫车都会说什么。" +
                                "\n.h8: 删除一个对话记录。";
                        sender.SENDER.sendGroupMsg(groupMsg, menu + msg);
                        break;
                    case ".h1":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “签到” 向猫车打个招呼吧~猫车会赠送你一些见面礼的。");
                        break;
                    case ".h2":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “查询”  即可查看自己的一些个人信息。比如包包里有多少金币？");
                        break;
                    case ".h3":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “命运 金币数量”  来测试一下自己的运气吧 ！");
                        break;
                    case ".h4":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “偷窃#@对方”  即可偷取别人的金币，对了小心不要被对方发现哦！");
                        break;
                    case ".h5":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “转账#@对方#金币数量”  如果你有富足的金币而无处使用，不如赠送一点给猫车吧！");
                        break;
                    case ".h6":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “记录#你想设置的问题#你想猫车做出的回答”  来教猫车,比如  记录#猫车我爱你#猫车也爱主人呢！对话记录需遵循不包含色情，赌博，政治及辱骂人等内容。");
                        break;
                    case ".h7":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “查询#一个记录的问题”  来查看这个问题猫车都有哪些回复。");
                        break;
                    case ".h8":
                        sender.SENDER.sendGroupMsg(groupMsg, "发送指令 “删除#一个记录的问题”  来删除一个已记录问题的所有回答。");
                        break;
                    case "自闭":
                        Random ran = new Random();
                        int i = ran.nextInt((10) + 1) * 60;
                        sender.SETTER.setGroupBan(groupMsg.getGroupInfo().getGroupCode(), groupMsg.getAccountInfo().getAccountCode(), i);
                        break;
                }
            }
            if (groupMsg.getGroupInfo().getGroupCode().equals("287834472")){
                //System.out.println(groupMsg.getMsg());
                //sender.SENDER.sendPrivateMsg("531065605",groupMsg.getOriginalData());
            }
            if (filter.include(groupMsg.getMsg(), 1)){
                List<String> words = filter.wordList(groupMsg.getMsg());
                int result = filter.wordCount(groupMsg.getMsg(), 1);
                //sender.SENDER.sendGroupMsg(groupMsg,"你输入的内容经猫车鉴定包含敏感词："+words+"\n敏感词数："+result);
            }else {
                    List<String> list_1 = new ArrayList<>(data.selMsg(groupMsg.getMsg()));
                    Random random = new Random();
                    int i = random.nextInt(list_1.size())+1;
                    String msg = filter.replace(list_1.get(i));
                    sender.SENDER.sendGroupMsg(groupMsg, msg);
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

        /**
     * 命运游戏签到功能
     * */
    @OnGroup
    @Filter(value = "签到")
    public void GroupMessage(GroupMsg groupMsg,MsgSender sender){
        try {
           System.out.println(groupMsg.getGroupInfo().getGroupCode()
           +"\n"+groupMsg.getAccountInfo().getAccountNickname()
           +"\n"+groupMsg.getMsg());
            long datee = System.currentTimeMillis();
            String Name = groupMsg.getAccountInfo().getAccountNickname();
            String QQ = groupMsg.getAccountInfo().getAccountCode();
            //String Balance = "2000";
            String UID = "10000";
            String Number = "5";
            String Favor = "5";
            Random r = new Random();
            String Balance = String.valueOf(r.nextInt(4000) + 2000);
            Date date = new Date();
            SimpleDateFormat dt = new SimpleDateFormat("yyyMMddHHmmss");
            SimpleDateFormat dr = new SimpleDateFormat("yyyMMdd");
            String time = dt.format(date);
            String times = dr.format(date);
        if (!groupMsg.getAccountInfo().getAccountCode().equals(data.QQ(QQ))) {
                data.addPlayer_info(Name, QQ, Balance, time, UID, Number, Favor);
                long date_1 = System.currentTimeMillis();
                sender.SENDER.sendGroupMsg(groupMsg,
                        "签到成功！本次签到获得：\n" + Balance + "金币，余额：" + Balance + "金币，" + "偷窃次数：" + Number + "好感度" + Favor + "\n本次执行时间：" + (date_1 - datee) + "ms");
            } else {
                List<String> list = new ArrayList<>(data.Player_info(QQ));
                long timess = Long.parseLong(list.get(3));
                long timea = Long.parseLong((Long.parseLong(times) - 1) + "235959");
                if (timess >= timea) {
                    long date_1 = System.currentTimeMillis();
                    Random I = new Random();
                    int i = I.nextInt(2);
                    if (i == 0) {
                        sender.SENDER.sendGroupMsg(groupMsg, "你已经签到过了，明天再来吧！" + "\n本次执行时间：" + (date_1 - datee) + "ms");
                    }
                    if (i == 1) {
                        sender.SENDER.sendGroupMsg(groupMsg, "不签，再签打死。");
                    }
                } else {
                    List<String> list_1 = new ArrayList<>(data.Player_info(QQ));
                    //金币
                    String balance = String.valueOf(Integer.parseInt(list_1.get(2)) + Integer.parseInt(Balance));
                    //好感度
                    String Favors = String.valueOf(Integer.parseInt(list_1.get(6)) + 5);
                    //盗窃次数
                    String Numbers = String.valueOf(Integer.parseInt(list_1.get(5)) + 5);
                    data.upPlayer_info(Name, QQ, balance, time, UID, Number, Favors);
                    long date_1 = System.currentTimeMillis();
                    sender.SENDER.sendGroupMsg(groupMsg,
                          "签到成功！本次签到获得：\n" + Balance + "金币，余额：" + balance + "金币，" + "偷窃次数：" + Numbers + "，好感度" + Favor + "\n本次执行时间：" + (date_1 - datee) + "ms");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 命运游戏查询金币余额
     * */
    @OnGroup
    @Filter(value = "查询")
    public void seletMY(GroupMsg groupMsg,MsgSender sender){
        try {
            if (data.QQ(groupMsg.getAccountInfo().getAccountCode()) != null) {
                long date = System.currentTimeMillis();
                List<String> list_1 = new ArrayList<>(data.Player_info(groupMsg.getAccountInfo().getAccountCode()));
                long date_1 = System.currentTimeMillis();
                sender.SENDER.sendGroupMsg(groupMsg,
                        "您的信息：\n"
                                + "昵称：" + groupMsg.getAccountInfo().getAccountNickname()
                                + "\n金币：" + list_1.get(2)
                                + "\n好感度：" + list_1.get(6)
                                + "\n盗窃次数：" + list_1.get(5)
                                + "\n本次执行时间：" + (date_1 - date) + "ms"
                                + "\n发送  菜单  获取更多玩法");
                }else {
                sender.SENDER.sendGroupMsg(groupMsg,
                        "你应该先签到。");
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 命运游戏
     * */
    @OnGroup
    @Filter(value = "命运.{0,}",matchType = MatchType.REGEX_FIND)
    public void my(GroupMsg groupMsg,MsgSender sender){
       // if(groupMsg.getGroupInfo().getGroupCode().equals("287834472")) {
            try {

                if (data.QQ(groupMsg.getAccountInfo().getAccountCode()) != null) {
                    System.out.println(groupMsg.getMsg());
                    long date = System.currentTimeMillis();
                    Random random = new Random();//创建随机数对象
                    int ran = random.nextInt(12) + 1;//获取1-100的随机数
                    Pattern p = Pattern.compile("[^0-9]");
                    Pattern q = Pattern.compile("[\\-]]");
                    Matcher m = p.matcher(groupMsg.getMsg());
                    String mm = m.replaceAll("-").trim();
                    //sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),mm);
                    long ba = Long.parseLong(m.replaceAll("").trim());
                    //long ba = Long.parseLong(groupMsg.getMsg().substring(3));//将String类型转换位int类型
                    long balance = Long.parseLong(data.selBalance(groupMsg.getAccountInfo().getAccountCode()));
                    if (balance >= 1) {
                        if (balance >= ba) {
                            if (ran == 1) {
                                String balancs_1 = dy.add_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "看来幸运女神在微笑！ \n增加：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 2) {
                                String balancs_1 = dy.add_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "欧神降临，该死的西方神！ \n增加：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 3) {
                                String balancs_1 = dy.add_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "哇哦，好可爱的土拨鼠！ \n增加：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 4) {
                                String balancs_1 = dy.add_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "福星高照，恭喜恭喜！ \n增加：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 5) {
                                String balancs_1 = dy.add_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "哇哦，真是一个走了狗屎运的小子！ \n增加：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 6) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "命运好像被扭曲了，真可惜！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 7) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "绿皮矮子，你的金币是我的了！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 8) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "兄台，你额头发黑啊，哈哈哈哈哈！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 9) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "真可惜，就差一点！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 10) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "愚蠢的土拨鼠，“AI”无可匹敌！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 11) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "年轻的赌鬼哟，看来金斧头和银斧头都不是你的！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 12) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "看来你的手气有点臭啊，小鬼！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 13) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "命运时常弄人，而你却想掌控命运！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            } else if (ran == 14) {
                                String balancs_1 = dy.subtract_balance(groupMsg.getAccountInfo().getAccountCode(), ba);
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                        + "你是酋长吗? 我想是的，非酋！  \n扣除：" + ba + "金币，余额：" + balancs_1 + "金币" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            }
                        } else {
                            long date_1 = System.currentTimeMillis();
                            sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()
                                    + "憨憨，你没有那么多金币啦！" + "\n本次执行时间：" + (date_1 - date) + "ms");
                        }
                    }
                }else {
                    sender.SENDER.sendGroupMsg(groupMsg,"你应该先签到。");
                }
            } catch (Exception e) {
                sender.SENDER.sendGroupMsg(groupMsg, "猫车不知道你在说什么呢！不如输入指令：命运 1  试试吧！");
            }
        //}
    }

    /**
     * 好友添加申请
     *  同意申请
     * */
    @OnFriendAddRequest
    public void addFriend(FriendAddRequest friend, MsgSender sender){
        try {
        long date = System.currentTimeMillis();
        sender.SETTER.setFriendAddRequest(friend.getFlag(),friend.getAccountInfo().getAccountNickname(),true,false);
        long date_1 = System.currentTimeMillis();
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群添加申请
     *  同意申请
     * */
    @OnGroupAddRequest
    public void AddGroup(GroupAddRequest addRequest,MsgSender sender){
        try {
                System.out.println(addRequest.getOriginalData());
                GroupMemberInfo info = sender.GETTER.getMemberInfo(addRequest.getGroupInfo().getGroupCode(), "911677204");
                if (!info.getPermission().isMember()) {
                    sender.SETTER.setGroupAddRequest(addRequest.getFlag(), true, false, "");
                    sender.SENDER.sendPrivateMsg("531065605", "入群申请："
                            + "\n申请人：\n" + addRequest.getAccountInfo().getAccountCode()
                            + "(" + (addRequest.getAccountInfo().getAccountNickname())
                            + ")\n申请群：\n"
                            + addRequest.getGroupInfo().getGroupCode()
                            + "(" + (addRequest.getGroupInfo().getGroupName()) + ")");
                } else {
                    sender.SETTER.setGroupAddRequest(addRequest.getFlag(), true, false, "");
                    sender.SENDER.sendPrivateMsg("531065605", "有人有加群了，但是猫车没有权限哟！"
                            + "群号：" + addRequest.getGroupInfo().getGroupCode()
                            + "群名：" + addRequest.getGroupInfo().getGroupName()
                            + "申请人：" + addRequest.getAccountInfo().getAccountCode());
                }
        } catch (Exception e) {
            if (filter.include(addRequest.getGroupInfo().getGroupName())) {}
            else {
                sender.SENDER.sendPrivateMsg("531065605", "有人邀请猫车进群了\n" +
                        "群号：" + addRequest.getGroupInfo().getGroupCode()
                        + "\n群名：" + addRequest.getGroupInfo().getGroupName());
                sender.SETTER.setGroupAddRequest(addRequest.getFlag(), true, false, "");
            }
        }
    }

    /**
     * 排行榜
     * */
    @OnGroup
    @Filter(value = "排行榜")
    public void balanceTop(GroupMsg groupMsg,MsgSender sender) {
        try {
            System.out.println(groupMsg.getMsg());
            long date = System.currentTimeMillis();
            List<Integer> list = new ArrayList<>(data.selBalance());
            Collections.sort(list);
            List<String> top = new ArrayList();
            for (int i = list.size(); i >= list.size() - 10; i--) {
                String name = data.balance_QQ(Integer.parseInt(String.valueOf(list.get(i - 1))));

                top.add(name + "：" + list.get(i - 1));
            }
            long date_1 = System.currentTimeMillis();
            sender.SENDER.sendGroupMsg(groupMsg, top.get(0) + "\n" + top.get(1) + "\n" + top.get(2) + "\n" + top.get(3) + "\n" + top.get(4) + "\n" + top.get(5) + "\n" + top.get(6) + "\n" + top.get(7) + "\n" + top.get(8) + "\n" + top.get(9) + "\n本次执行时间：" + (date_1 - date) + "ms");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     *      命运游戏金币偷取
     * */
    @OnGroup
    @Filter(value = "偷.{0,}#.{0,}",matchType = MatchType.REGEX_FIND)
    public void stealBalance(GroupMsg groupMsg,MsgSender sender){
        try {

            if (data.QQ(groupMsg.getAccountInfo().getAccountCode()) != null) {
                System.out.println(groupMsg.getMsg());
                long date = System.currentTimeMillis();
                String qq = dy.atCode(groupMsg.getMsg());
                String[] msg = qq.split("#");
                //获取偷窃发起人信息
                List<String> s_blance = new ArrayList<>(data.Player_info(groupMsg.getAccountInfo().getAccountCode()));
                if (data.QQ(msg[0]) != null) {
                //获取被偷窃人信息
                List<String> b_blance = new ArrayList<>(data.Player_info(msg[0]));
                Random ran = new Random();
                int blance_num = ran.nextInt(1000) + 1;//随机盗窃的金币
                int probability = ran.nextInt(10) + 1;//盗窃成功的几率
                int number = Integer.parseInt(s_blance.get(5));//查询盗窃者的次数
                if (!groupMsg.getAccountInfo().getAccountCode().equals(msg[0])) {
                    if (probability != 6) {//如果随机数不等于6
                        if (Integer.parseInt(b_blance.get(2)) != 0) {//如果被盗人金币
                            if (Integer.parseInt(b_blance.get(2)) >= blance_num) {
                                if (Integer.parseInt(s_blance.get(5)) >= 1) {
                                    int number_1 = number - 1;
                                    String s_blance_1 = String.valueOf(Integer.parseInt(s_blance.get(2)) + blance_num);
                                    data.upPlayer_info(s_blance.get(0), s_blance.get(1), s_blance_1, s_blance.get(3), s_blance.get(4), String.valueOf(number_1), s_blance.get(6));
                                    //
                                    String b_blance_1 = String.valueOf(Integer.parseInt(b_blance.get(2)) - blance_num);
                                    data.upPlayer_info(b_blance.get(0), b_blance.get(1), b_blance_1, b_blance.get(3), b_blance.get(4), b_blance.get(5), b_blance.get(6));
                                    long date_1 = System.currentTimeMillis();
                                    sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "偷窃成功！本次偷盗获得：" + blance_num + "金币，你的金币余额：" + s_blance_1 + "你可偷窃次数：" + number_1 + "\n本次执行时间：" + (date_1 - date) + "ms");
                                } else {
                                    long date_1 = System.currentTimeMillis();
                                    sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "你的偷窃次数不足！" + "\n本次执行时间：" + (date_1 - date) + "ms");
                                }
                            } else {
                                long date_1 = System.currentTimeMillis();
                                sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "偷窃失败，目标金币不多啦！" + "\n本次执行时间：" + (date_1 - date) + "ms");
                            }
                        } else {
                            long date_1 = System.currentTimeMillis();
                            sender.SENDER.sendGroupMsg(groupMsg, "偷窃目标的金币余额不足，请换个目标吧！" + "\n本次执行时间：" + (date_1 - date) + "ms");
                        }
                    } else {
                        int number_1 = number - 1;
                        if (Integer.parseInt(s_blance.get(2)) < blance_num) {
                            String s_blance_1 = String.valueOf(Integer.parseInt(s_blance.get(2)) - Integer.parseInt(s_blance.get(2)));
                            String b_blance_1 = String.valueOf(Integer.parseInt(b_blance.get(2)) + Integer.parseInt(s_blance.get(2)));
                            data.upPlayer_info(s_blance.get(0), s_blance.get(1), s_blance_1, s_blance.get(3), s_blance.get(4), String.valueOf(number_1), s_blance.get(6));
                            data.upPlayer_info(b_blance.get(0), b_blance.get(1), b_blance_1, b_blance.get(3), b_blance.get(4), b_blance.get(5), b_blance.get(6));
                            long date_1 = System.currentTimeMillis();
                            sender.SENDER.sendGroupMsg(groupMsg, "偷窃失败，你笨拙的动作令" + msg[0] + "产生了怀疑，并抓住你交给了猫车大人，无奈只能交罚款" + s_blance.get(2) + "金币重获自由！" + "\n本次执行时间：" + (date_1 - date) + "ms");

                        } else {
                            String s_blance_1 = String.valueOf(Integer.parseInt(s_blance.get(2)) - blance_num);
                            String b_blance_1 = String.valueOf(Integer.parseInt(b_blance.get(2)) + blance_num);
                            data.upPlayer_info(s_blance.get(0), s_blance.get(1), s_blance_1, s_blance.get(3), s_blance.get(4), String.valueOf(number_1), s_blance.get(6));
                            data.upPlayer_info(b_blance.get(0), b_blance.get(1), b_blance_1, b_blance.get(3), b_blance.get(4), b_blance.get(5), b_blance.get(6));
                            long date_1 = System.currentTimeMillis();
                            sender.SENDER.sendGroupMsg(groupMsg, "偷窃失败，你笨拙的动作令" + msg[0] + "产生了怀疑，并抓住你交给了猫车大人，无奈只能交罚款" + blance_num + "金币重获自由！" + "\n本次执行时间：" + (date_1 - date) + "ms");
                        }
                    }

                } else {
                    long date_1 = System.currentTimeMillis();
                    sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "憨憨，你会偷你自己的钱吗？" + "\n本次执行时间：" + (date_1 - date) + "ms");
                }
                }else {
                    sender.SENDER.sendGroupMsg(groupMsg,"偷窃失败!猫车没有找到关于"+msg[0]+"的信息。");
                }
            }else {
                sender.SENDER.sendGroupMsg(groupMsg,"你应该先签到。");
            }
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg,"猫车不知道你在说什么呢！不如输入指令：偷#@531065605  试试吧！");
        }
    }

    /**
     *      对话记录
     * */
    @Filter(value = "记录#.{0,}#.{0,}",matchType = MatchType.REGEX_FIND)
    @OnGroup
    public void addMsg(GroupMsg groupMsg,MsgSender sender){
        try {
            System.out.println(groupMsg.getGroupInfo().getGroupCode()+groupMsg.getGroupInfo().getGroupName()
                    +"\n"+groupMsg.getAccountInfo().getAccountCode()+groupMsg.getAccountInfo().getAccountNickname()
                    +"\n"+groupMsg.getMsg());
            String[] m = groupMsg.getMsg().split("#");
            String Msg_1 = dy.shield(m[1]);
            String Msg_2 = dy.shield(m[2]);
            if (filter.include(m[2])) {
                List<String> words = filter.wordList(m[2]);
                int result = filter.wordCount(m[2], 1);
                sender.SENDER.sendGroupMsg(groupMsg,"你输入的内容经猫车鉴定包含敏感词："+words+"\n敏感词数："+result+"\n记录失败。");
            }else {
                    data.addMsg(Msg_1, Msg_2);
                    sender.SENDER.sendGroupMsg(groupMsg, "记录成功！猫车更聪明了呢！");
            }
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg,"猫车不知道你在说什么呢！不如输入指令：记录#猫车#我爱你  试试吧！");
        }}

    /**
     *      金币充值
     * */
    @Filter(value = "充值#.{0,}#.{0,}",matchType = MatchType.REGEX_FIND)
    @OnGroup
    public void addBalance(GroupMsg groupMsg,MsgSender sender){
        try {
            System.out.println(groupMsg.getMsg());
            long date = System.currentTimeMillis();
            String qq = dy.atCode(groupMsg.getMsg());
            String[] msg = qq.split("#");
            //获取被被充值人信息
            List<String> list = new ArrayList<>(data.Player_info(msg[0]));
            if (groupMsg.getAccountInfo().getAccountCode().equals("531065605")){
                String Balance = String.valueOf(Integer.parseInt(list.get(2)) +Integer.parseInt(msg[1]));
                data.upPlayer_info(list.get(0),msg[0],Balance,list.get(3),list.get(4), list.get(5),list.get(6));
                long date_1 = System.currentTimeMillis();
                sender.SENDER.sendGroupMsg(groupMsg,"充值成功！"+msg[0]+"的金币余额为："+Balance+ "\n本次执行时间：" + (date_1 - date) + "ms");
            }
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg,"猫车不知道你在说什么呢！不如输入指令：充值#@531065605#500  试试吧！");}
    }

    /**
     *      金币更改
     * */
    @Filter(value = "金币#.{0,}#.{0,}",matchType = MatchType.REGEX_FIND)
    @OnGroup
    public void UpBalance(GroupMsg groupMsg,MsgSender sender){
        try {
            long date = System.currentTimeMillis();
            String qq = dy.atCode(groupMsg.getMsg());
            String[] msg = qq.split("#");
            //获取信息
            List<String> list = new ArrayList<>(data.Player_info(msg[0]));
            if (groupMsg.getAccountInfo().getAccountCode().equals("531065605")){
                String Balance = msg[1];
                data.upPlayer_info(list.get(0),msg[0],Balance,list.get(3),list.get(4), list.get(5),list.get(6));
                long date_1 = System.currentTimeMillis();
                sender.SENDER.sendGroupMsg(groupMsg,"金币修改成功！"+msg[0]+"的金币余额为："+Balance+ "\n本次执行时间：" + (date_1 - date) + "ms");
                System.out.println(groupMsg.getMsg());
            }
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg,"猫车不知道你在说什么呢！不如输入指令：金币#@531065605#500  试试吧！");}
    }

    /**
     *  金币转账
     * */
    @Filter(value = "转账#.{0,}#.{0,}",atBot = true,groups = "287834472",matchType = MatchType.REGEX_FIND)
    @OnGroup
    public void tranBalance(GroupMsg groupMsg,MsgSender sender){
        try {

            System.out.println(groupMsg.getMsg());
            long date = System.currentTimeMillis();
            String qq = dy.atCode(groupMsg.getMsg());
            //System.out.println(qq);
            String[] msg = qq.split("。");
            //获取转账发起人信息
            List<String> list_1 = new ArrayList<>(data.Player_info(groupMsg.getAccountInfo().getAccountCode()));
            //获取转账接受人信息
            //System.out.println(data.QQ(msg[0])+"hhh");
            if (data.QQ(msg[0]) != null) {
                List<String> list = new ArrayList<>(data.Player_info(msg[0]));
                if (Integer.parseInt(list_1.get(2)) >= Integer.parseInt(msg[1]) && !groupMsg.getAccountInfo().getAccountCode().equals(msg[0])) {
                    String list_1_balance = String.valueOf(Integer.parseInt(list_1.get(2)) - Integer.parseInt(msg[1]));
                    String list_balance = String.valueOf(Integer.parseInt(list.get(2)) + Integer.parseInt(msg[1]));
                    data.upPlayer_info(list_1.get(0), list_1.get(1), list_1_balance, list_1.get(3), list_1.get(4), list_1.get(5), list_1.get(6));
                    data.upPlayer_info(list.get(0), list.get(1), list_balance, list.get(3), list.get(4), list.get(5), list.get(6));
                    long date_1 = System.currentTimeMillis();
                    sender.SENDER.sendGroupMsg(groupMsg, "转账成功！\n" + groupMsg.getAccountInfo().getAccountCode() + "的金币余额为：" + list_1_balance + "\n本次执行时间：" + (date_1 - date) + "ms");
                } else {
                    sender.SENDER.sendGroupMsg(groupMsg, "转账失败！");
                }
            }else {
                sender.SENDER.sendGroupMsg(groupMsg,"转账失败!猫车没有找到关于"+msg[0]+"的信息。");
            }
        }catch (Exception e){
            e.printStackTrace();
            sender.SENDER.sendGroupMsg(groupMsg,"猫车不知道你在说什么呢！不如输入指令：转账#@531065605#500  试试吧！");
        }
    }

    /**
     *      通过问题删除所有回答
     * */
    @OnGroup
    @Filter(value = "删除#.{0,}",matchType = MatchType.REGEX_FIND)
    public void delMsg(GroupMsg groupMsg,MsgSender sender) {
        try {
            System.out.println(groupMsg.getMsg());
            long date = System.currentTimeMillis();
            String[] msg = groupMsg.getMsg().split("#");
            data.delMessage_1(msg[1]);
            long date_1 = System.currentTimeMillis();
            sender.SENDER.sendGroupMsg(groupMsg, "删除成功!\n本次执行时间：" + (date_1 - date) + "ms");
        } catch (Exception e) {
            String[] msg = groupMsg.getMsg().split("#");
            sender.SENDER.sendGroupMsg(groupMsg,"删除【"+msg[1]+"】出现错误!");
        }
    }

    /**
     *      通过问题查询所有回答
     * */
    @OnGroup
    @Filter(value = "查询#.{0,}",matchType = MatchType.REGEX_FIND)
    public void selMsg_1(GroupMsg groupMsg,MsgSender sender) {
        try {
            System.out.println(groupMsg.getMsg());
            long date = System.currentTimeMillis();
            String[] msg = groupMsg.getMsg().split("#");
            if (msg.length==2) {
                List<String> list = new ArrayList(data.selMsg(msg[1]));
                StringBuffer Msg = new StringBuffer("问题：\n").append(msg[1]).append("\n回复：\n");
                for (int I = 1; I < list.size(); I++) {
                    if (list.get(I) != null) {
                        Msg.append(list.get(I)).append("\n");
                    }
                }
                long date_1 = System.currentTimeMillis();
                sender.SENDER.sendGroupMsg(groupMsg, Msg + "\n本次执行时间：" + (date_1 - date) + "ms");
            }else {
                sender.SENDER.sendGroupMsg(groupMsg,
                        "至少输入一个需要查询的问题吧。");
            }
        } catch (Exception e) {
            String[] msg = groupMsg.getMsg().split("#");
            sender.SENDER.sendGroupMsg(groupMsg,"猫车没有查询到与【"+msg[1]+"】相关的记录!");
        }
    }

    /**
     *
     * 监听猫车是否被禁言
     */
    @Listen(GroupMute.class)
    public void BotBan(GroupMute muteGet, MsgSender sender){
        try {
            if (muteGet.getAccountInfo().getAccountCode().equals("911677204")){
                sender.SETTER.setGroupQuit(muteGet.getGroupInfo().getGroupCode(),true);
                sender.SENDER.sendPrivateMsg("531065605", "猫车被禁言啦,已退出群:"
                        + "\n群号：" + muteGet.getGroupInfo().getGroupCode()
                        + "\n群名：" + muteGet.getGroupInfo().getGroupName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 好友增加事件
     * */
    @OnFriendIncrease
    public void FriendIncrease(FriendIncrease friendIncrease, MsgSender sender) {
        try {
            String name = friendIncrease.getAccountInfo().getAccountNicknameAndRemark();
            sender.SENDER.sendPrivateMsg("531065605", "有人添加猫车好友了：\n" + friendIncrease.getOriginalData());
            System.out.println("有人添加猫车好友了：" + name + "\n" + friendIncrease.getAccountInfo().getAccountCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出群聊
     */
    @OnPrivate
    @Filter(value = "退群#.{0,}")
    public void RetGroup(PrivateMsg msg,MsgSender sender) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
