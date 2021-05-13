/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.boot;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.Context;

import apijson.framework.APIJSONParser;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;

import apijson.Log;
import apijson.NotNull;
import apijson.StringUtil;
import apijson.demo.DemoFunctionParser;
import apijson.demo.DemoParser;
import apijson.demo.DemoSQLConfig;
import apijson.demo.DemoSQLExecutor;
import apijson.demo.DemoVerifier;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.AbstractVerifier;
import apijson.orm.FunctionParser;
import apijson.orm.Parser;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import apijson.orm.Verifier;


/**SpringBootApplication
 * 右键这个类 > Run As > Java Application
 * @author Lemon
 */
@Configuration
@SpringBootApplication
public class DemoApplication implements ApplicationContextAware, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
	private static final String TAG = "DemoApplication";

	static {
		// APIJSON 配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		Map<String, Pattern> COMPILE_MAP = AbstractVerifier.COMPILE_MAP;
		COMPILE_MAP.put("PHONE", StringUtil.PATTERN_PHONE);
		COMPILE_MAP.put("EMAIL", StringUtil.PATTERN_EMAIL);
		COMPILE_MAP.put("ID_CARD", StringUtil.PATTERN_ID_CARD);

		// 使用本项目的自定义处理类
		APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator() {

			@Override
			public Parser<Long> createParser() {
				return new DemoParser();
			}

			@Override
			public FunctionParser createFunctionParser() {
				return new DemoFunctionParser();
			}

			@Override
			public Verifier<Long> createVerifier() {
				return new DemoVerifier();
			}

			@Override
			public SQLConfig createSQLConfig() {
				return new DemoSQLConfig();
			}

			@Override
			public SQLExecutor createSQLExecutor() {
				return new DemoSQLExecutor();
			}

		};
	}


	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);

		// FIXME 不要开放给项目组后端之外的任何人使用 UnitAuto（强制登录鉴权）！！！如果不需要单元测试则移除相关代码或 unitauto.Log.DEBUG = false;
		// 上线生产环境前改为 false，可不输出 APIJSONORM 的日志 以及 SQLException 的原始(敏感)信息
		APIJSONParser.IS_PRINT_BIG_LOG = true;
		APIJSONApplication.init();
	}

	// SpringBoot 2.x 自定义端口方式
	@Override
	public void customize(ConfigurableServletWebServerFactory server) {
		server.setPort(8080);
	}

	// 全局 ApplicationContext 实例，方便 getBean 拿到 Spring/SpringBoot 注入的类实例
	private static ApplicationContext APPLICATION_CONTEXT;
	public static ApplicationContext getApplicationContext() {
		return APPLICATION_CONTEXT;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		APPLICATION_CONTEXT = applicationContext;		
	}


	// 支持 APIAuto 中 JavaScript 代码跨域请求 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOriginPatterns("*")  
				.allowedMethods("*")
				.allowCredentials(true)
				.maxAge(3600);  
			}
		};
	}

	// 支持 APIAuto 中 JavaScript 代码跨域请求 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
