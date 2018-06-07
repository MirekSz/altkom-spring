package pl.altkom.shop.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "thread")
public class ThreadRequestContextImpl extends RequestContextImpl {

}
