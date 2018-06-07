package pl.altkom.shop.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request")
public class RequestContextImpl implements RequestContext {

	@Override
	public void hello() {
		// TODO Auto-generated method stub

	}

}
