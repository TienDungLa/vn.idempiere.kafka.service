package vn.idempiere.kafka.activator;

import org.adempiere.base.Core;
import org.idempiere.process.IMappedProcessFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import vn.idempiere.kafka.process.KafkaGenColumnProcess;
import vn.idempiere.kafka.process.ResendMessageProcess;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		IMappedProcessFactory mappedFactory = Core.getMappedProcessFactory();
		mappedFactory.addMapping(KafkaGenColumnProcess.class.getName(), () -> new KafkaGenColumnProcess());
		mappedFactory.addMapping(ResendMessageProcess.class.getName(), () -> new ResendMessageProcess());

	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
