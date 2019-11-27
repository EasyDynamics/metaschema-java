package gov.nist.secauto.metaschema.model.xml;

import java.math.BigInteger;

import gov.nist.csrc.ns.oscal.metaschema.x10.AssemblyDocument;
import gov.nist.csrc.ns.oscal.metaschema.x10.XmlGroupBehavior;
import gov.nist.secauto.metaschema.datatype.MarkupString;
import gov.nist.secauto.metaschema.model.AbstractAssemblyInstance;
import gov.nist.secauto.metaschema.model.InfoElementDefinition;
import gov.nist.secauto.metaschema.model.JsonGroupBehavior;

public class XmlAssemblyInstance extends AbstractAssemblyInstance {
//	private static final Logger logger = LogManager.getLogger(XmlAssemblyInstance.class);

	private final AssemblyDocument.Assembly xAssembly;


	public XmlAssemblyInstance(AssemblyDocument.Assembly xAssembly, InfoElementDefinition parent) {
		super(parent);
		this.xAssembly = xAssembly;
	}

	@Override
	public String getName() {
		return getXmlAssembly().getRef();
	}

	@Override
	public String getFormalName() {
		return getDefinition().getFormalName();
	}

	@Override
	public MarkupString getDescription() {
		MarkupString retval = null;
		if (getXmlAssembly().isSetDescription()) {
			retval = MarkupStringConverter.toMarkupString(getXmlAssembly().getDescription());
		} else if (isReference()) {
			retval = getDefinition().getDescription();
		}
		return retval;
	}

	@Override
	public int getMinOccurs() {
		int retval = 0;
		if (getXmlAssembly().isSetMinOccurs()) {
			retval = getXmlAssembly().getMinOccurs().intValueExact();
		}
		return retval;
	}

	@Override
	public int getMaxOccurs() {
		int retval = 1;
		if (getXmlAssembly().isSetMaxOccurs()) {
			Object value = getXmlAssembly().getMaxOccurs();
			if (value instanceof String) {
				// unbounded
				retval = -1;
			} else if (value instanceof BigInteger) {
				retval = ((BigInteger)value).intValueExact();
			}
		}
		return retval;
	}

	@Override
	public String getInstanceName() {
		return getXmlAssembly().isSetGroupAs() ? getXmlAssembly().getGroupAs().getName() : getName();
	}

	protected AssemblyDocument.Assembly getXmlAssembly() {
		return xAssembly;
	}

	@Override
	public JsonGroupBehavior getGroupBehaviorJson() {
		JsonGroupBehavior retval = JsonGroupBehavior.SINGLETON_OR_LIST;
		if (getXmlAssembly().isSetGroupAs() && getXmlAssembly().getGroupAs().isSetInJson()) {
			retval = JsonGroupBehavior.lookup(getXmlAssembly().getGroupAs().getInJson());
		}
		return retval;
	}

	@Override
	public boolean isGroupBehaviorXmlGrouped() {
		// the default
		boolean retval = true;
		if (getXmlAssembly().isSetGroupAs() && getXmlAssembly().getGroupAs().isSetInXml()) {
			retval = XmlGroupBehavior.GROUPED.equals(getXmlAssembly().getGroupAs().getInXml());
		}
		return retval;
	}
}
