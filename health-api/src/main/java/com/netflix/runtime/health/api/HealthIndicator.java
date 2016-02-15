package com.netflix.runtime.health.api;

/**
 * Basic interface for defining health indication logic.  0 or more HealthIndicators are used to determine 
 * the application health. HealthIndicators are tracked by a {@link HealthIndicatorRegistry}
 * where the default implementation uses all HealthIndicators registered as a set multibinding.  
 * 
 * HealthIndicator can inject types that are to be consulted for health indication, call out to shell 
 * scripts or call a remote service.  
 * 
 * To register a health indicator,
 * <code>
 * Multbindings.newSetBinder(binder()).addBinding().to(MyHealthIndicator.class);
 * </code>
 *  
 * Here is a sample health indicator implementation. 
 * 
 * <code>
 * public class MyHealthIndicator implements HealthIndicator {
 *     {@literal @}Inject
 *     public MyHealthIndicator(MyService service) {
 *         this.service = service;
 *     }
 *     
 *     {@literal @}Inject
 *     
 *     public CompletableFuture{@literal <}HealthIndicatorStatus{@literal >} check(HealthIndicatorCallback healthCallback) {
 *          if (service.getErrorRate() {@literal >} 0.1) {
 *              healthCallback.inform(Health.unhealthy()
 *              			  				.withDetails("errorRate", service.getErrorRate()));
 *          }
 *          else {
 *              healthCallback.inform(Health.healthy());
 *          }
 *     }
 * }
 * </code>
 * 
 * @author elandau
 */
public interface HealthIndicator {
    /**
     * Inform the provided {@link HealthIndicatorCallback} of the {@link Health}.
     * 
     * Implementations should catch exceptions and return a status of unhealthy to provide customized messages. 
     * Uncaught exceptions will be captured by the default implementation of {@link HealthCheckAggregator} and 
     * returned as an unhealthy status automatically. 
     * 
     * Implementations of {@link HealthCheckAggregator} will also handle threading and timeouts for implementations 
     * of {@link HealthIndicator}. Each {@link HealthIndicator} will be run in its own thread with a timeout. 
     * Implementations should not spawn additional threads (or at least take responsibility for killing them).
     * Timeouts will result in an unhealthy status being returned for any slow {@link HealthIndicator}
     * with a status message indicating that it has timed out. 
     */
    void check(HealthIndicatorCallback healthCallback);

}