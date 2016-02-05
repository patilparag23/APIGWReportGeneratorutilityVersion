package reportGenerator;



public class Util {
	
	//MONITORING END URLs
	public static final String MONITORING_HEALTH_CHECK = "/monitoring/healthcheck";
	public static final String MONITORING_FORCE_ERROR = "/monitoring/forceerror";
	public static final String MONITORING_STATUS = "/monitoring/status";
	//MESSAGE END URLs
	public static final String OTAC_EMAIL_REDEEM = "/otac/email/redeem";
	public static final String OTAC_PHONE_REDEEM = "/otac/phone/redeem";
	public static final String OTAC_EMAIL_ISSUE	= "/otac/email/issue";
	public static final String OTAC_PHONE_ISSUE = "/otac/phone/issue";
	public static final String VX_SHORTCODE_AUTHCODE_REDEEM= "/vX/shortcode/authcode/redeem";
	public static final String MESSAGE_EMAIL = "/message/email";
	public static final String MESSAGE_SMS = "/message/sms";
	public static final String XMLRPC = "/xmlrpc";
	//AUTH END URLs
	public static final String CHECK= "/check";
	public static final String AUTH_SPOOF_ADMIN = "/auth/spoof/admin";
	public static final String AUTH_SPOOF_DELEGATE = "/auth/spoof/delegate";
	public static final String AUTH_SPOOF = "/auth/spoof";
	public static final String AUTH_OTAC_SPOOF = "/auth/otac/spoof";
	public static final String AUTH_OTAC = "/auth/otac";
	public static final String Auth_TAC_ISSUE="/auth/tac";
	public static final String AUTH_SELECT_OTHER = "/auth/select/other";
	public static final String AUTH_SELECT = "/auth/select";
	public static final String AUTH_CHALLENGE = "/auth/challenge";
	public static final String AUTH_ADDITIONAL_PASSWORD_O2 = "/auth/additional_password_o2";
	public static final String AUTH_PASSWORD_O2 = "/auth/password_o2";
	public static final String AUTH_ADDITIONAL_CHALLENGE = "/auth/additional_challenge";
	public static final String TOKEN_CURRENT = "/token/CURRENT";
	public static final String TOKEN_PRODUCT_PAYMMBB = "/token/product/PAYMMBB";
	public static final String TOKEN = "/token";
	public static final String USERINFO = "/userinfo";
	public static final String USER_SPOOFED_LIMIT = "/user/SPOOFED/limit";
	public static final String USER_SPOOFED_LOCK = "/user/SPOOFED/lock";
	public static final String USER_CURRENT_LOCK = "/user/CURRENT/lock";
	public static final String USER_CURRENT_LIMIT = "/user/CURRENT/limit";
	public static final String USER_CURRENT_AUTHCODE = "/user/CURRENT/authcode";
	public static final String USER_ADDITIONAL_AUTHCODE= "/user/ADDITIONAL/authcode";
	public static final String AUTHCODE_AUTHCODE = "/authcode/AUTHCODE";
	public static final String IMPLICITAUTH = "/implicitauth";
	public static final String AUTHREQUIREMENT= "/authrequirement";
	//IDENTITY END URLs
	public static final String CUSTOMER_UNRESTRICTED_PRODUCT = "/customer/UNRESTRICTED/product";
	public static final String CUSTOMER_PHONE_PROOF_PRODUCTS = "/customer/PHONE_PROOF/products";
	public static final String CUSTOMER_ADDITIONAL_PRODUCT = "/customer/ADDITIONAL/product";
	public static final String INVALIDATE = "/invalidate";
	public static final String IDENTITY_OTAC_CHALLENGE_WITHPRODUCTS = "/identity/OTAC/challenge/withproducts";
	public static final String IDENTITY_OTAC_CHALLENGE = "/identity/OTAC/challenge";
	public static final String IDENTITY_OTAC_RECOVERABLE_EMAIL = "/identity/OTAC/recoverable/email";
	public static final String IDENTITY_OTAC_RECOVERABLE_PHONE = "/identity/OTAC/recoverable/phone";
	public static final String IDENTITY_AUTH_PASSWORD = "/identity/auth/password";
	public static final String IDENTITY_AUTH_CHALLENGE = "/identity/auth/challenge";
	public static final String IDENTITY_INTERNAL_VERIFY_EMAIL = "/identity/INTERNAL/verify/email";
	public static final String IDENTITY_ADDITIONAL= "/identity/ADDITIONAL";
	public static final String IDENTITY_MOR= "/identity/mor";
	public static final String IDENTITY = "/identity";
	public static final String VX_CUSTOMER_CURRENT_PRODUCT_CLAIMABLE = "/vX/customer/CURRENT/product/claimable";
	public static final String SECURITYQUESTION = "/securityquestion";
	public static final String IDENTITY_PAYMMBB = "/product/PAYMMBB";
	public static final String IDENTITY_PRODUCT= "/product";
	public static final String IDENTITY_PERSON="/vX/person/CURRENT/phone";
}
