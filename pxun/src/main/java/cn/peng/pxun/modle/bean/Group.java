package cn.peng.pxun.modle.bean;

import cn.bmob.v3.BmobObject;

/**
 * 群组实体
 * Created by msi on 2017/9/23.
 */

public class Group extends BmobObject{
    private String groupNum;
    private String groupName;
    private String groupDesc;
    private String groupIcon;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    private String groupType;

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }
}
