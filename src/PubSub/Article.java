package PubSub;

public final class Article {
	
	private Category type;
	private String originator;
	private String org;
	private String content;
	boolean isValid = false;
	
	public Article(String originator, String org, Category category, String content) {
		this.type = category;
		this.originator = originator;
		this.org = org;
		this.content = content;
	}
	
	public Article(String message){
		String[] article = message.trim().toLowerCase().split(";");
		if (article.length <= 4 && article.length >0){
			this.isValid = true;
			this.type = setType(article[0]);
			this.originator = (article.length >=2 && !article[1].equals("")) ? article[1] : null;
			this.org = (article.length >=3 && !article[2].equals("")) ? article[2] : null;
			this.content = (article.length >=4 && !article[3].equals("")) ? article[3] : null;
		}
	}
	
	private Category setType(String type) {
		if (type.equals("sports")){
			return Category.Sports;
		}else if(type.equals("lifestyle")){
			return Category.Lifestyle;
		}else if(type.equals("entertainment")){
			return Category.Entertainment;
		}else if(type.equals("business")){
			return Category.Business;
		}else if(type.equals("health")){
			return Category.Health;
		}else if(type.equals("politics")){
			return Category.Politics;
		}else if(type.equals("science")){
			return Category.Science;
		}else if(type.equals("technology")){
			return Category.Technology;
		}else {
			return Category.None;
		}
	}
	
	public String getOriginator() {
		return originator;
	}
	
	public Category getType(){
		return type;
	}
	
	public String getOrg() {
		return org;
	}
	
	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "Article: [originator=" + originator + ", org=" + org + ", type=" + type.toString() + ", content=" + content
				+ "]";
	}
	

}
