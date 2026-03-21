package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.HostProfileResponse;
import com.ruralxperience.entity.HostProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostProfileMapper {

    @Mapping(target = "userId",     source = "user.id")
    @Mapping(target = "firstName",  source = "user.firstName")
    @Mapping(target = "lastName",   source = "user.lastName")
    @Mapping(target = "avatarUrl",  source = "user.avatarUrl")
    HostProfileResponse toResponse(HostProfile hostProfile);
}
