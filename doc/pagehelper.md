## pagehelper配置

### [参考配置](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md)

### yml配置
~~~yaml
pagehelper:
  # 数据库方言 mysql
  helperDialect: mysql
  # 分页参数合理化，默认是false
  # 启用合理化时，如果pageNum < 1会查询第一页，如果pageNum > pages会查询最后一页；
  # 禁用合理化时，如果pageNum < 1 或 pageNum > pages会返回空数据
  reasonable: true
  # 支持通过Mapper接口参数来传递分页参数，默认值false
  supportMethodsArguments: true
  # 为了支持startPage(Object params)方法，增加了该参数来配置参数映射，用于从对象中根据属性名取值
  # 默认值countSql
  params: count=countSql
~~~