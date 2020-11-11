package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.cdteam.spring.cloud.starter.common.utils.BeanCopyUtils;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.cdteam.spring.cloud.starter.context.constant.ResponseCodeEnum;
import org.cdteam.spring.cloud.starter.context.exception.AppException;
import org.curriculumdesign.bookserp.base.domain.BookTypeEntity;
import org.curriculumdesign.bookserp.base.domain.ClientEntity;
import org.curriculumdesign.bookserp.base.dto.ClientDTO;
import org.curriculumdesign.bookserp.base.mapper.ClientMapper;
import org.curriculumdesign.bookserp.base.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 出版社 服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientMapper mapper;

    @Override
    public Pagination<ClientDTO> page(Integer pageSize, Integer pageNum, String name, String contactName, String contactMobile) {
        Page<ClientEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<ClientEntity> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(ClientEntity::getName, name);
        }
        if (StringUtils.isNotBlank(contactName)) {
            queryWrapper.like(ClientEntity::getContactName, contactName);
        }
        if (StringUtils.isNotBlank(contactMobile)) {
            queryWrapper.like(ClientEntity::getContactMobile, contactMobile);
        }
        IPage<ClientEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<ClientEntity> records = bookEntityIPage.getRecords();
        List<ClientDTO> bookDTOS = ListUtils.transferList(records, ClientDTO.class);
        Pagination<ClientDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }

    @Override
    public Integer save(ClientDTO clientDTO) {
        if (isExist(clientDTO.getName(), clientDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "客户名称已存在");
        }
        ClientEntity clientEntity = BeanCopyUtils.transferBean(clientDTO, ClientEntity.class);
        if (clientDTO.getId() == null) {
            return mapper.insert(clientEntity);
        }
        return mapper.updateById(clientEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<ClientDTO> list() {
        List<ClientEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<ClientDTO> clientDTOS = ListUtils.transferList(records, ClientDTO.class);
        return clientDTOS;
    }

    @Override
    public ClientDTO getById(Long id) {
        ClientEntity entity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(entity, ClientDTO.class);
    }

    private boolean isExist(String name, Long id) {
        LambdaQueryWrapper<ClientEntity> queryWrapper = Wrappers.<ClientEntity>lambdaQuery().eq(ClientEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(ClientEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
