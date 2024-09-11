package io.summernova.admin.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.common.exception.BusinessException;
import io.summernova.admin.common.protocal.Result;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.context.BaseContextContainer;
import io.summernova.admin.core.field.Many2Many;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.field.util.ReadOnlyUtil;
import io.summernova.admin.core.field.util.RelationFieldUtil;
import io.summernova.admin.core.mapper.RelationMapper;
import io.summernova.admin.core.mapper.RelationMapperRegistry;
import io.summernova.admin.core.method.util.MethodUtil;
import io.summernova.admin.core.model.BaseModel;
import io.summernova.admin.core.protocal.query.Condition;
import io.summernova.admin.core.protocal.query.OrderBys;
import io.summernova.admin.core.service.BaseService;
import io.summernova.admin.core.service.BaseServiceRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static io.summernova.admin.common.exception.CoreBusinessExceptionEnums.ERROR_INVALID_ARGUMENTS;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
@RestController
@Tag(name = "通用接口")
@RequestMapping("/common")
public class CommonController implements BaseContextContainer {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public CommonController(
            @Qualifier("mappingJackson2HttpMessageConverterObjectMapper") ObjectMapper objectMapper,
            Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Operation(summary = "Select by IDs")
    @GetMapping("/{modelName}")
    public <T extends BaseModel> Result<List<T>> select(
            @PathVariable @NotEmpty String modelName,
            @RequestParam @NotEmpty @Size(max = 1000) List<Long> ids
    ) throws IllegalAccessException {
        BaseService<T> service = BaseServiceRegistry.getByModelName(modelName);
        List<T> records = service.selectByIds(ids);
        if (records != null && !records.isEmpty()) {
            fetchMany2One(service, records);
            fetchMany2Many(service, records);
        }
        return Result.success(records);
    }

    @Operation(summary = "Page Query")
    @GetMapping("/{modelName}/page")
    public <T extends BaseModel> Result<IPage<T>> page(
            @PathVariable @NotEmpty String modelName,
            @RequestParam @Positive @Max(1000) Long pageNum,
            @RequestParam @Positive @Max(1000) Long pageSize,
            @RequestParam(required = false) String orderBys,
            @RequestBody(required = false) Condition condition
    ) throws IllegalAccessException {
        BaseService<T> service = BaseServiceRegistry.getByModelName(modelName);
        OrderBys orderBys1 = orderBys != null ? OrderBys.parse(orderBys) : null;
        IPage<T> page = service.page(pageNum, pageSize, condition, orderBys1);
        List<T> records = page.getRecords();
        if (records != null && !records.isEmpty()) {
            fetchMany2One(service, records);
            fetchMany2Many(service, records);
        }
        return Result.success(page);
    }

    @Operation(summary = "Count Query")
    @GetMapping("/{modelName}/count")
    public Result<Long> count(
            @PathVariable @NotEmpty String modelName,
            @RequestBody(required = false) Condition condition
    ) {
        BaseService<?> service = BaseServiceRegistry.getByModelName(modelName);
        return Result.success(service.count(condition));
    }

    @Operation(summary = "Name Search")
    @GetMapping("/{modelName}/nameSearch")
    public <T extends BaseModel> Result<List<T>> nameSearch(
            @PathVariable @NotEmpty String modelName,
            @RequestParam String name
    ) {
        BaseService<T> service = BaseServiceRegistry.getByModelName(modelName);
        List<T> records = service.nameSearch(name);
        return Result.success(records);
    }

    @Operation(summary = "Create Records")
    @PostMapping("/{modelName}")
    @Validated(CreateValidationGroup.class)
    public <T extends BaseModel> Result<List<T>> create(
            @PathVariable @NotEmpty String modelName,
            @RequestBody @NotEmpty String createDtoListData
    ) throws IOException {
        BaseService<T> service = BaseServiceRegistry.getByModelName(modelName);
        Class<T> modelClass = service.getModelClass();

        // Deserialize createDtoList
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, modelClass);
        List<T> createDtoList = objectMapper.readValue(createDtoListData, listType);

        // Validate createDtoList
        Set<ConstraintViolation<DtoListValidateWrapper>> violations =
                validator.validate(new DtoListValidateWrapper(createDtoList), CreateValidationGroup.class);
        if (!violations.isEmpty())
            throw new BusinessException(ERROR_INVALID_ARGUMENTS);

        // Validate read-only fields
        checkReadOnlyForDtoList(service, createDtoList);

        // Do create
        service.createBatch(createDtoList);
        return Result.success(createDtoList);
    }

    @Operation(summary = "Update Records")
    @PutMapping("/{modelName}")
    @Validated(UpdateValidationGroup.class)
    public <T extends BaseModel> Result<Boolean> update(
            @PathVariable @NotEmpty String modelName,
            @RequestBody @NotEmpty String updateDtoListData
    ) throws IOException {
        BaseService<T> service = BaseServiceRegistry.getByModelName(modelName);
        Class<T> modelClass = service.getModelClass();

        // Deserialize updateDtoList
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, modelClass);
        List<T> updateDtoList = objectMapper.readValue(updateDtoListData, listType);

        // Validate updateDtoList
        Set<ConstraintViolation<DtoListValidateWrapper>> violations =
                validator.validate(new DtoListValidateWrapper(updateDtoList), UpdateValidationGroup.class);
        if (!violations.isEmpty())
            throw new BusinessException(ERROR_INVALID_ARGUMENTS);

        // Validate read-only fields
        checkReadOnlyForDtoList(service, updateDtoList);

        // Do update
        for (T t : updateDtoList) {
            Long id = t.getId();
            t.setId(null);
            service.updateById(id, t);
        }
        return Result.success(true);
    }

    @Operation(summary = "Delete Records")
    @DeleteMapping("/{modelName}")
    public Result<Boolean> delete(
            @PathVariable @NotEmpty String modelName,
            @RequestParam @NotEmpty List<Long> ids
    ) {
        BaseService<?> service = BaseServiceRegistry.getByModelName(modelName);
        return Result.success(service.deleteByIds(ids));
    }

    @Operation(summary = "Call Service Method")
    @PostMapping("/{modelName}/{methodName}")
    public Result<Object> callServiceMethod(
            @PathVariable @NotEmpty String modelName,
            @PathVariable @NotEmpty String methodName,
            @RequestBody(required = false) List<String> params
    ) {
        BaseService<?> service = BaseServiceRegistry.getByModelName(modelName);
        log.info("Rpc request received: service={}, method={}, params={}", service.getClass().getSimpleName(), methodName, params);

        Method method;
        Object[] args;
        if (params == null) {
            method = MethodUtil.getPublicMethod(service, methodName, 0);
            args = EMPTY_ARGS;
        } else {
            method = MethodUtil.getPublicMethod(service, methodName, params.size());
            args = getArgs(method, params);
        }

        Object result;
        try {
            result = method.invoke(service, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return Result.success(result);
    }

    private Object[] getArgs(Method method, List<String> params) {
        Object[] args = new Object[params.size()];
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == String.class) {
                args[i] = params.get(i);
                continue;
            }

            try {
                args[i] = objectMapper.readValue(params.get(i), parameterTypes[i]);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return args;
    }

    private <T extends BaseModel> void fetchMany2One(BaseService<T> service, List<T> records) throws IllegalAccessException {
        Class<T> modelClass = service.getModelClass();

        for (Field field : RelationFieldUtil.getMany2OneFields(modelClass)) {
            // Get target ids
            Set<Long> targetIds = new HashSet<>();
            for (T record : records) {
                Long targetId = ((Many2One<?>) field.get(record)).getId();
                if (targetId != null && targetId > 0) {
                    targetIds.add(((Many2One<?>) field.get(record)).getId());
                }
            }

            // Get target records
            BaseService<?> targetService = service.getService(RelationFieldUtil.getTargetModelClass(modelClass, field));
            List<? extends BaseModel> targetRecords = targetService.selectByIds(targetIds.stream().toList());
            Map<Long, ? extends BaseModel> targetRecordsMap = targetRecords.stream()
                    .collect(Collectors.toMap(BaseModel::getId, t -> t));

            // Set target records to many2one field
            for (T record : records) {
                @SuppressWarnings("unchecked")
                Many2One<BaseModel> many2One = (Many2One<BaseModel>) field.get(record);
                Long targetId = many2One.getId();
                if (targetId != null && targetId > 0) {
                    field.set(record, Many2One.ofRecord(targetRecordsMap.get(targetId)));
                }
            }
        }
    }

    private <T extends BaseModel> void fetchMany2Many(BaseService<T> service, List<T> records) throws IllegalAccessException {
        Class<T> modelClass = service.getModelClass();
        for (Field field : RelationFieldUtil.getMany2ManyFields(modelClass)) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(modelClass, field);
            RelationMapper relationMapper = RelationMapperRegistry.getMapper(modelClass, targetClass);

            // Get all target ids and the map of record id -> target ids
            Set<Long> allTargetIds = new HashSet<>();
            Map<Long, List<Long>> recordId2TargetIdsMap = new HashMap<>();
            for (T record : records) {
                List<Long> targetIds = relationMapper.getTargetIds(modelClass, record.getId());
                allTargetIds.addAll(targetIds);
                recordId2TargetIdsMap.put(record.getId(), targetIds);
            }

            // Get allTargetRecords
            BaseService<?> targetService = service.getService(targetClass);
            List<? extends BaseModel> allTargetRecords = targetService.selectByIds(allTargetIds.stream().toList());
            Map<Long, ? extends BaseModel> targetRecordsMap = allTargetRecords.stream()
                    .collect(Collectors.toMap(BaseModel::getId, t -> t));

            // Set many2many field
            for (T record : records) {
                List<Long> targetIds = recordId2TargetIdsMap.get(record.getId());
                List<? extends BaseModel> targetRecords = targetIds.stream()
                        .map(targetRecordsMap::get).collect(Collectors.toList());
                field.set(record, Many2Many.ofRecords(targetRecords));
            }
        }
    }

    private <T extends BaseModel> void checkReadOnlyForDtoList(BaseService<T> service, List<T> dtoList) {
        Class<T> modelClass = service.getModelClass();
        for (Field field : ReadOnlyUtil.getReadOnlyFields(modelClass)) {
            for (T dto : dtoList) {
                try {
                    if (field.get(dto) != null)
                        throw new BusinessException(ERROR_INVALID_ARGUMENTS);
                } catch (IllegalAccessException e) {
                    // this should never happen
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private record DtoListValidateWrapper(@NotEmpty List<@Valid ? extends BaseModel> dtoList) {
    }
}
