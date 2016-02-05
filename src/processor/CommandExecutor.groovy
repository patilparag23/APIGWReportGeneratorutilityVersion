package processor

class CommandExecutor {

	public static void main(String[] args){
		commanExecutor();
	}

	public static void commanExecutor(){

		Map<String,Integer> map=new HashMap<String,Integer>();

		List<String> list=new ArrayList<String>();

		//println Integer.MAX_LENGTH

		def flag=false;

		def process=new ProcessBuilder("/home/cts1/reddy/scripts/fetchIBMProviders.sh").redirectErrorStream(true).start()
		process.inputStream.eachLine {
			String str=it;
			if(str.equals("abc") || flag){
				//println it
				if(flag)
					list.add(str.trim());
				flag=true;
			}
		}

		for(String str:list){

			String[] strArray=str.split(" ");

			if(map.get(strArray[1].trim().substring(0,1))!=null){
				Integer coun=map.get(strArray[1].trim().substring(0,1))+Integer.parseInt(strArray[0].trim());
				map.put(strArray[1].trim().substring(0,1),coun);
			}
			else if(strArray[1].trim().equals("499")){
				map.put("5",Integer.parseInt(strArray[0].trim()));
			}else{
				map.put(strArray[1].trim().substring(0,1),Integer.parseInt(strArray[0].trim()));
			}
			//if(map.get(" 2")

			//println strArray
			//println strArray[0]
			//println str;
		}

		//println list.size();
		
		println map;
	}
}
