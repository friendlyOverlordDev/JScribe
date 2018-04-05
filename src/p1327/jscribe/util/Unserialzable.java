package p1327.jscribe.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public interface Unserialzable extends Externalizable {
	
	@Override
	default void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		throw new UnsupportedOperationException("Can't deserialize this object.");
	}
	
	@Override
	default void writeExternal(ObjectOutput out) throws IOException {
		throw new UnsupportedOperationException("Can't serialize this object.");
	}
}
