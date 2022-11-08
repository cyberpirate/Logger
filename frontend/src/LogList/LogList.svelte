
<script lang="ts">
    import Drawer, {
        AppContent,
        Content,
        Header,
        Title,
        Subtitle,
    } from '@smui/drawer';
    import Button, { Label } from '@smui/button';
    import List, { Item, Text } from '@smui/list';
    import { drawerOpen, selectedLog } from "../stores";

</script>


<AppContent class="app-content">
    <main class="main-content">
        <Button on:click={() => (drawerOpen.update((value) => !value))}>
            <Label>Toggle Drawer</Label>
        </Button>
        <br />
        <pre class="status">Active: {$selectedLog}</pre>
    </main>
</AppContent>

<style>
    * :global(.app-content) {
        flex: auto;
        overflow: auto;
        position: relative;
        flex-grow: 1;
    }

    .main-content {
        overflow: auto;
        padding: 16px;
        height: 100%;
        box-sizing: border-box;
    }
</style>