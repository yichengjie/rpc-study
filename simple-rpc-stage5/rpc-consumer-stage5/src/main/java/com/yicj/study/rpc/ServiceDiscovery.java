package com.yicj.study.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery {
	//private Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
	
	private static final String registryPath = "/rpc";
	@Autowired
	private ConnectManage connectManage;
	@Autowired
	private ZkClient zkClient ;

	@PostConstruct
	private void init() {
		this.subscribeChildChanges();
	}

	public void subscribeChildChanges() {
		List<String> nodeList = zkClient.subscribeChildChanges(registryPath, new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				updateConnectedServer(currentChilds, registryPath);
			}
		});
		// 将现有的地址保存起来
		updateConnectedServer(nodeList, registryPath);
	}

	private void updateConnectedServer(List<String> currentChilds, String nodepath) {
		List<String> addressList = getNodeData(currentChilds, nodepath);
		connectManage.updateConnectServer(addressList);
	}

	private List<String> getNodeData(List<String> nodes, String nodepath) {
		List<String> retList = new ArrayList<String>();
		for (String node : nodes) {
			String address = zkClient.readData(nodepath + "/" + node);
			retList.add(address);
		}
		return retList;
	}

	public static void main(String[] args) throws IOException {
	}

}
