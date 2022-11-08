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

    $: console.log("" + $drawerOpen);

</script>


<Drawer variant="dismissible" open={$drawerOpen}>
    <Header>
      <Title>Super Drawer</Title>
      <Subtitle>It's the best drawer.</Subtitle>
    </Header>
    <Content>
      <List>
        <Item
          href="javascript:void(0)"
          on:click={() => selectedLog.set('Gray Kittens')}
          activated={$selectedLog === 'Gray Kittens'}
        >
          <Text>Gray Kittens</Text>
        </Item>
        <Item
          href="javascript:void(0)"
          on:click={() => selectedLog.set('A Space Rocket')}
          activated={$selectedLog === 'A Space Rocket'}
        >
          <Text>A Space Rocket</Text>
        </Item>
        <Item
          href="javascript:void(0)"
          on:click={() => selectedLog.set('100 Pounds of Gravel')}
          activated={$selectedLog === '100 Pounds of Gravel'}
        >
          <Text>100 Pounds of Gravel</Text>
        </Item>
        <Item
          href="javascript:void(0)"
          on:click={() => selectedLog.set('All of the Shrimp')}
          activated={$selectedLog === 'All of the Shrimp'}
        >
          <Text>All of the Shrimp</Text>
        </Item>
        <Item
          href="javascript:void(0)"
          on:click={() => selectedLog.set('A Planet with a Mall')}
          activated={$selectedLog === 'A Planet with a Mall'}
        >
          <Text>A Planet with a Mall</Text>
        </Item>
      </List>
    </Content>
  </Drawer>