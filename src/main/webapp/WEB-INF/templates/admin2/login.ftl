<!doctype html>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/static/imagebook/admin2/css/login.css" />
		<title>Имиджбук</title>
    </head>
    <body>
  	    <div class="main">
			<div class="form">
				<h1>Имиджбук</h1>
				<form name="f" method="POST" action='/admin2/auth' class="login-form">
					<table>
						<tr>
							<td class="label-column">Логин:</td>
							<td><input id="login-input" class="login-input" type="text" name="username" value="" size="20" /></td>
						</tr>
						<tr>
							<td>Пароль:</td>
							<td><input class="password-input" type="password" name="password" /></td>
						</tr>
					</table>
				    <input class="login-button" name="submit" type="submit" value="Войти" />
				    <#if loginFailed??><div class="login-failed">Неверный логин или пароль</div></#if>
				</form>
			</div>
		</div>
		
		<script type="text/javascript">
			document.getElementById("login-input").focus();
		</script>
    </body>
</html>