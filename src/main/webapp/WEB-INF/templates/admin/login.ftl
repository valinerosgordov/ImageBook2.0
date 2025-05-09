<!doctype html>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name='gwt:property' content='locale=ru'>
		<title>${serviceName} - вход в систему управления</title>
        <link rel="stylesheet" type="text/css" href="/static/core/css/core.css">
        <link rel="stylesheet" type="text/css" href="/static/core/css/login.css" />
    </head>
    <body>
  	    <div class="main">
			<div class="form">
				<h1>${serviceName}</h1>
				<h2>Система управления</h2>
				<form name='f' action='/admin/auth' method='POST'>
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
				    <input class="form-submit" name="submit" type="submit" value="Войти" />
				    <#if loginFailed??><div class="login-failed">Неверный логин или пароль</div></#if>
				</form>
			</div>
		</div>

        <script type="text/javascript">
            document.getElementById("login-input").focus();
        </script>
	</body>
</html>