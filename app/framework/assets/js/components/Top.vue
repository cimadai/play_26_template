<template>
    <div id="top_vue">
        <div class="jumbotron">
            <div class="container">
                <h1 v-cloak>{{ greeting }}</h1>
            </div>
        </div>
        <div class="container">
            <input type="text" class="form-control" v-model="greeting"/>
        </div>
        <div>
            <a href="/api/users">API: Users</a>
        </div>
    </div>
</template>

<script type="text/tsx">
    export default {
        data: () => {
            return {
                greeting: "Hello World! Please edit me."
            }
        }
    };
</script>
