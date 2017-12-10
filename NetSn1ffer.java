import java.net.*;
import java.util.*;
 
public class NetSniffer {	
	
    public static void main(String[] args) {
		Resolver.resolveAll();
		Reporter.reportAll();
		Sniffer.sniffReachableHostMachines();
	}
    
}
 
class Reporter {
	
	public static void reportAll()
	{
		print("<----NetSniffer Report---->");
		reportHostname();
		reportAllIpAddresses();
		reportReachableIPs();
	}
		
	public static void reportHostname()
	{
		print("Hostname --> " + HostData.hostname);
	}
	
	public static void reportAllIpAddresses()
	{
		int counter;
		counter = 0;
		
		print("<----All Ip addresseses of host---->");
		
		for (InetAddress inet: HostData.ipaddresses)
		{
			
			print(counter + " Ip address --> " + inet.getHostAddress());
			counter++;
		}
	}
	
	public static void reportReachableIPs()
	{
		try
		{
			print("<----Printing reachable host Ip addresses, if any---->");
			for (InetAddress inet: HostData.resolvedMachines)
			{
				print("Host machine at --> " + inet.getHostAddress() + " is reachable.");
			}
			print("<----Done printing reachable host machine Ip addresses, if any---->");
		} catch (Exception e)
		{
			print("Error printing reachable Ip's --> " + e.toString());
		}
	}
	
	public static void reportListeningPort(int port)
	{
		print("Port --> " + port + " is listening on host.");
	}
	
	public static void print(Object object)
	{
		System.out.println(object);
	}
}
 
class Sniffer {
    
	public static void sniffReachableHostMachines()
	{
		Socket s = null;
		print("<---Scanning reachable host machines---->");
		try
		{
		    for(InetAddress inet: HostData.resolvedMachines)
		    {
		    	for (int cport = 0; cport < 65000; cport++)
		    	{
		    		//System.out.print(".");
		    		try
				    {
		    		    s = new Socket();
				        s.setPerformancePreferences(0, 0, 1);
				        s.setKeepAlive(false);
				        s.setSoTimeout(1);
				        s.setReuseAddress(false);
				        s.setSoLinger(false, 0);
		    			
				        s.connect(new InetSocketAddress(inet.getHostAddress(), cport), 1);
				        Reporter.reportListeningPort(cport);
				    }
		    		
		    		catch (Exception e)
		    		{}
		    	} 
		    }
		    print("<----Done scanning reachable host machines---->");
		    s.close();
		} 
		catch (Exception e)
		{
			print("Error sniffing host machines --> " + e.toString());
		}
		
	}
	
    public static void print(Object object)
	{
		System.out.println(object);
	}
}
 
class Resolver {
	
	public static void resolveAll()
	{
		resolveHostname();
		resolveInet(HostData.hostname);
		resolveAllIpAddresses(HostData.inet);
		resolveReachableHostMachines();
	}
	
	/*Finds and captures all if any ip addresses of the host*/
    public static void resolveAllIpAddresses(InetAddress inet)
    {
    	try
    	{
    	    HostData.ipaddresses =
    	    		InetAddress.getAllByName(HostData.hostname);
    	} catch (Exception e)
    	{
    		print("Error resolving Ip addresses --> " + e.toString());
    	}
    }
    
    /*Captures the hostname.*/
    public static void resolveHostname()
    {
    	try 
    	{
    		Scanner sc = new Scanner(System.in);
    		print("Enter the hostname. Ex. 123.423.132.123 or www.google.com");
    		while (true)
    		{
    		    if (sc.hasNext())
    		    {
        	    	HostData.hostname = sc.next();
        	    	sc.close();
        	    	break;
    		    }
    		}
    		
    	} catch (Exception e)
    	{
    		print("Error recieving hostname --> " + e.toString());
    	}
    }
    
    /*Checks the hostname to see if it is valid, then converts 
     * it to an ip address object. The object is stored away.*/
    public static void resolveInet(String hostname)
    {
    	try {
			HostData.inet = InetAddress.getByName(hostname);
		} catch (Exception e)
    	{
			print("Error resolving initial Ip address --> " + e.toString());
		}
    }
    
    /*Captures the InetAddress objects
     *that contain the ip's of the reachable machines.*/
    public static void resolveReachableHostMachines()
    {
    	try
		{
			for (InetAddress inet: HostData.ipaddresses)
			{
				if (inet.isReachable(5000))
				{
					HostData.resolvedMachines.add(inet);
				}
			}
		} catch (Exception e)
    	{
			print("Error resolving host machines --> " + e.toString());
    	}
    }
    
    public static void print(Object object)
	{
		System.out.println(object);
	}
    
}
 
class HostData {
	static String hostname;
	static InetAddress inet;
	static InetAddress[] ipaddresses;
	static ArrayList<InetAddress> resolvedMachines = new ArrayList<InetAddress>();
}
