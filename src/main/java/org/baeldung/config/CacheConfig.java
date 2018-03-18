package org.baeldung.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @see https://findusages.com/search/net.sf.ehcache.config.CacheConfiguration/timeToLiveSeconds$1
 *      https://www.programcreek.com/java-api-examples/index.php?class=net.sf.ehcache.config.CacheConfiguration&method=setTimeToLiveSeconds
 * https://memorynotfound.com/spring-caching-example-java-xml-configuration
 */

@Configuration
@EnableCaching
public class CacheConfig {

//    @Bean
//    public net.sf.ehcache.CacheManager ehCacheManager() {
//        CacheConfiguration cacheConfiguration = new CacheConfiguration();
//        cacheConfiguration.setName("tasks");
//        cacheConfiguration.transactionalMode(CacheConfiguration.TransactionalMode.OFF);
//        cacheConfiguration.setMaxEntriesLocalHeap(100);
//        cacheConfiguration.timeToLiveSeconds(3);
//        cacheConfiguration.timeToIdleSeconds(0);
////        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
//        cacheConfiguration.setMemoryStoreEvictionPolicy("LFU");
//
//        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
//        config.addCache(cacheConfiguration);
//
//        return net.sf.ehcache.CacheManager.newInstance(config);
//    }
}
