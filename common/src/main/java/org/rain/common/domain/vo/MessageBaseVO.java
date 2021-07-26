package org.rain.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageBaseVO {
    private int id;
    private int msgType;//1连接，2公共，3单独
    private String goalClient;
    private String msgId;
    private String clientName;
    private String clientId;
    //暂时在单机情况下不会使用
    private String serverId;
    private String message;
    private Long timestamp;
}
