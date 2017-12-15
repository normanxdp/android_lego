package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * 二级消息实体 文 件 名: SecondMsg.java 描 述: <描述>
 * 
 * @author: chenzy
 * @date: 2015-10-15 当消息类型为1（系统通知）时候，返回如下数据：
 *        {"code":"0","msg":"","data":[{"Id":"1"
 *        ,"AllyId":"1","MemberId":"1","Title":"加入俱乐部申请审核结果通知"
 *        ,"Subtitle":"审核通过啦","Pic":
 *        "http:\/\/img.bnplus.cn\/2015\/10\/03\/f20662684224921e4de9a9d4f495071e.jpg","Time":"1444401945","IsRead":"0"}
 *        ] }
 *        属性含义：Id：通知消息唯一标识，在删除与设置已读状态请传入该值做为objid；Title：显示的主标题；Subtitle：显示的子标题
 * 
 * 
 *        当消息类型为2（关注）时候，返回如下数据：
 *        {"code":"0","msg":"","data":[{"FocusId":"1","AllyId"
 *        :"1","MemberId":"1","FriendId":"2","Time"
 *        :"1444401945","IsRead":"0","FriendRealName":"洪智","FriendAvatar":
 *        "http:\/\/img.quickwinner.cn\/2015\/09\/10\/2083b986b58a85d7037c2b41663c9cfd.jpg"}]
 *        } 属性含义：FocusId：关注唯一标识，在删除与已读的时候传该值做为objid的值
 *        ；MemberId：被关注会员的编号；FriendId
 *        ：发起关注的会员编号，FriendAvatar：发起关注会员的头像；FriendRealName：发起关注会员的姓名
 * 
 * 
 *        当消息类型为8（加入俱乐部申请）时候，返回如下数据：
 *        {"code":"0","msg":"","data":[{"Id":"1","AllyId"
 *        :"1","MemberId":"1","ClubId":"1","ClubName"
 *        :"台球俱乐部","ApplyMemberId":"2"
 *        ,"Time":"1444401945","IsRead":"0","ApplyMemberRealName"
 *        :"洪智","ApplyMemberAvatar":
 *        "http:\/\/img.quickwinner.cn\/2015\/09\/10\/2083b986b58a85d7037c2b41663c9cfd.jpg"}]
 *        } 属性含义：Id：申请记录的唯一标识，删除、已读的时候用该属性做为objid的值；MemberId：审核人（俱乐部秘书）的会员编号；
 *        ApplyMemberId：申请加入俱乐部的会员编号， ClubId：申请加入的俱乐部编号
 */
public class SecondMsg implements Serializable {

	private int Id;
	private int MemberId; // 被关注会员的编号
	private String Title;
	private String SubTitle;
	private String Pic;
	private String Time;
	private String TopTime;
	private String IsRead;
	private String ObjId;

	public String getObjId() {
		return ObjId;
	}

	public void setObjId(String objId) {
		ObjId = objId;
	}

	//业务层数据
	private int DemId;


	public int getDemId() {
		return DemId;
	}
	public void setDemId(int demId) {
		DemId = demId;
	}

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}

	public int getMemberId() {
		return MemberId;
	}
	public void setMemberId(int memberId) {
		MemberId = memberId;
	}

	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getSubTitle() {
		return SubTitle;
	}
	public void setSubTitle(String subTitle) {
		SubTitle = subTitle;
	}

	public String getPic() {
		return Pic;
	}
	public void setPic(String pic) {
		Pic = pic;
	}

	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getTopTime() {
		return TopTime;
	}
	public void setTopTime(String time) {
		TopTime = time;
	}

	public String getIsRead() {
		return IsRead;
	}
	public void setIsRead(String isRead) {
		IsRead = isRead;
	}
}
