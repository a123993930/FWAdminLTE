package com.guy.common.persistence;

import com.guy.common.utils.StringUtils;
import org.hibernate.criterion.DetachedCriteria;

import java.util.HashMap;
import java.util.Map;

/**
* 解决Criteria的Alias依赖问题
* @author kenshin
*
*/
public class ResolveAlias {
        //用来缓存aliasName
        private Map<String,String> aliasCache = null;
        private DetachedCriteria dca;
        //只要使用一次就递增，防止关联的对象中出现同名属性，所以每个生成的别名后都会加一个count
        private int count = 0;
        public ResolveAlias(DetachedCriteria dca) {
                this.dca = dca;
                this.aliasCache = new HashMap<String,String>();
                this.count = 0;
        }
        /**
         * 递归创建alias，并且缓存到cahce中，给后面的关联用
         * 假设有属性        a.b.c.d        d为基本属性或者日期类型
         * 调用时应该传入 createAlias("a.b.c");
         * 执行顺序先从cache中查找a.b.c的aliasName，如果有直接返回，如果没有则递归获取a.b的aliasName，
         * 当一直递归从缓冲中获取a的aliasName都不存在，那么反向依次为a,a.b,a.b.c创建aliasName
         * @param association        一个关联字符串
         * @return                                指定关联的aliasName
         */
        public String createAlias(String association) {
                String aliasName = aliasCache.get(association);
                String thisAsso = null;
                if (StringUtils.isNotBlank(aliasName)) {
                        return aliasName;
                }else {
                        if (association.indexOf(".") != -1) {
                                //如果还包含"."则不是最后一层
                                //获取最后一个"."之前的字符串
                                String preAssociation = StringUtils.substringBeforeLast(association, ".");
                                //递归获取aliasName
                                String preAlias = createAlias(preAssociation);
                                //当前关联的是前一部分的关联的别名 + "." + 最后一个"."之后的部分
                                thisAsso = preAlias + "." + StringUtils.substringAfterLast(association, ".");
                                //创建别名
                                aliasName = StringUtils.substringAfterLast(association, ".") + count++;
                        }else {
                                //如果没有"."了，则表示已经到了最低层的关联
                                thisAsso = association;
                                aliasName = association + count++;
                        }
                        //为DetachedCriteria创建alias
                        dca.createAlias(thisAsso, aliasName);
                        //缓存到cache中
                        aliasCache.put(association, aliasName);
                        return aliasName;
                }
        }
        /**
         * 返回当前cache的副本
         * @return        cache的副本
         */
        public Map<String,String>  getAliasCacheCopy() {

        	Map<String,String>  map=this.aliasCache;
        	Map<String,String> cloneMap=new  HashMap<String,String>();
        	cloneMap.putAll(map);
        	return cloneMap;
        }
        /**
         * 返回当前count的值
         * @return        当前count的值
         */
        public int getAliasCount() {
                return this.count;
        }
}