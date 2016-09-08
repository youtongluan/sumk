package org.yx.listener;

import java.util.EventObject;

import org.yx.util.GsonUtil;

public class DBEvent extends EventObject
{
    private static final long serialVersionUID = 5467587679L;
    
    /**
     * 表名，非空
     */
    private String type;
    
    /**
     * 操作类型，非空
     */
    private String operate;
    
    /**
     * 主键id，可以为空
     */
    private String id;
    
    /**
     * @param source
     * @param type 表名，非空
     * @param operate 操作，非空
     * @param id
     */
    public DBEvent(Object source, String type, String operate, String id)
    {
        super(GsonUtil.copyObject(source));
        this.type = type;
        this.operate = operate;
        this.id = id;
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getOperate()
    {
        return operate;
    }
    
    /**
     * get事件中，id是空的
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }
    
    @Override
    public String toString()
    {
        return "DBEvent [type=" + type + ", operate=" + operate + ", id=" + id + "]";
    }
}
