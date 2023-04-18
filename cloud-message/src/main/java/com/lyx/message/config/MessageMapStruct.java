package com.lyx.message.config;

import com.lyx.message.entity.SystemMessage;
import com.lyx.message.entity.vo.SystemMessageVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapStruct {

    List<SystemMessageVO> systemMessageToVO(List<SystemMessage> systemMessages);

}
