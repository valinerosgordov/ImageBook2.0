<!doctype html>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name='gwt:property' content='locale=ru'>
		<title>${serviceName} - вход в онлайн-сборщик фотокниг</title>
		<link rel="stylesheet" type="text/css" href="/static/core/css/core.css">
		<link rel="stylesheet" type="text/css" href="/static/core/css/login.css" />
		<script language="javascript" src="/gwt.editor/gwt.editor.nocache.js"></script>
    </head>
    <body>
        ${clientParameters!}
        <div class="main">
			<div class="form">
				<h1>${serviceName}</h1>
				<h2>Онлайн-сборщик фотокниг</h2>
				<form name='f' action='${contextUrl}/auth' method='POST'>
					<table>
						<tr>
							<td width="120px">Логин:</td>
							<td><input id="login-input" type='text' name='username' value='' size="20" /></td>
						</tr>
						<tr>
							<td>Пароль:</td>
							<td><input id="password-input"  type='password' name='password' /></td>
						</tr>
					</table>
                  <div id="remember-me"><input id="checkbox" type='checkbox' name='_spring_security_remember_me'/> Запомнить меня на этом компьютере</div>
				  <div><input class="form-submit" name="submit" type="submit" value="Войти" /></div>
				  <#if loginFailed??><div class="login-failed">Неверный логин или пароль</div></#if>
				  <div id="register" class="register">&nbsp;</div>
				  <div id="restore" class="restore">&nbsp;</div>
				</form>
			</div>
		</div>

        <script type="text/javascript">
            document.getElementById("login-input").focus();
        </script>
	</body>
</html>