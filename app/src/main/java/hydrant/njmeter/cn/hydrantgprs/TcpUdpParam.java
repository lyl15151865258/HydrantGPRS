package hydrant.njmeter.cn.hydrantgprs;

public class TcpUdpParam {
	private String mode;
	private String ip1;
	private String ip2;
	private String ip3;
	private String ip4;
	private String port;
	private boolean isvalid;
	
	
	public TcpUdpParam(){
		isvalid = true;
	}
	
	public TcpUdpParam(String mode, String ip1, String ip2, String ip3,
                       String ip4, String port) {
		this.mode = mode;
		this.ip1 = ip1;
		this.ip2 = ip2;
		this.ip3 = ip3;
		this.ip4 = ip4;
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "\"" + mode + "\",\"" + ip1 + "."
				+ ip2 + "." + ip3 + "." + ip4 + "\",\"" + port
				+ "\"";
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		if(mode.equals("TCP")||mode.equals("UDP"))
			this.mode = mode;
		else
			this.isvalid = false;
	}

	public String getIp1() {
		return ip1;
	}

	public void setIp1(String ip1) {
		try{
			int ip = Integer.parseInt(ip1);
			if(ip>=0 && ip<=255)
				this.ip1 = ip1;
			else
				this.isvalid = false;
		}catch(Exception ex){
			this.isvalid = false;
		}
		
		
	}

	public String getIp2() {
		return ip2;
	}

	public void setIp2(String ip2) {
		try{
			int ip = Integer.parseInt(ip2);
			if(ip>=0 && ip<=255)
				this.ip2 = ip2;
			else
				this.isvalid = false;
		}catch(Exception ex){
			this.isvalid = false;
		}
	}

	public String getIp3() {
		return ip3;
	}

	public void setIp3(String ip3) {
		try{
			int ip = Integer.parseInt(ip3);
			if(ip>=0 && ip<=255)
				this.ip3 = ip3;
			else
				this.isvalid = false;
		}catch(Exception ex){
			this.isvalid = false;
		}
	}

	public String getIp4() {
		return ip4;
	}

	public void setIp4(String ip4) {
		try{
			int ip = Integer.parseInt(ip4);
			if(ip>=0 && ip<=255)
				this.ip4 = ip4;
			else
				this.isvalid = false;
		}catch(Exception ex){
			this.isvalid = false;
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		try{
			int ip = Integer.parseInt(port);
			if(ip>=0 && ip<=65535)
				this.port = port;
			else
				this.isvalid = false;
		}catch(Exception ex){
			this.isvalid = false;
		}
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}
	
}