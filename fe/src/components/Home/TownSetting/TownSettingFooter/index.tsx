import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTheme } from 'styled-components';

import Icon from '@atoms/Icon';
import PATH from '@constants/routerPath';
import { $FooterContainer, $ButtonContainer, $Button, $FooterName, $TownSettingInfo } from './TownSettingFooter.style';
import DialogPortal from '@components/portals/DialogPortal';
import Dialog from '@molecules/Dialog';

interface TownSettingFooterProps {
  towns: { townId: number; name: string }[];
}

const TownSettingFooter = ({ towns }: TownSettingFooterProps) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const [selectedTowns, setSelectedTowns] = useState(towns);
  const [selectedTownId, setSelectedTownId] = useState<number>();
  const [isConfirmShown, setIsConfirmShown] = useState(false);
  const [isAlertShown, setIsAlertShown] = useState(false);
  const handleTownButtonClick = (id: number) => {
    setSelectedTownId(id);
    if (selectedTowns.length === 1) {
      setIsAlertShown(true);
      return;
    }
    if (selectedTowns.length === 2) {
      setIsConfirmShown(true);
      return;
    }
    // TODO(jayden): 해당 id의 town DELETE 요청 추가
  };

  return (
    <$FooterContainer>
      <$FooterName>내 동네</$FooterName>
      <$ButtonContainer>
        {selectedTowns.map(town => (
          <$Button key={town.townId} onClick={() => handleTownButtonClick(town.townId)} size="large" status="active">
            {town.name.split(' ')[2]}
            <Icon name="cancel" fill={theme.COLORS.NEUTRAL.BACKGROUND.DEFAULT} />
          </$Button>
        ))}
        {selectedTowns.length === 1 && (
          <$Button
            onClick={() => navigate(PATH.HOME.TOWN_SEARCH, { state: { towns: selectedTowns } })}
            size="large"
            status="default"
          >
            동네 추가
            <Icon name="plus" fill={theme.COLORS.NEUTRAL.TEXT.STRONG} />
          </$Button>
        )}
      </$ButtonContainer>
      <$TownSettingInfo>※ 지역은 최소 1개, 최대 2개까지 설정 가능해요.</$TownSettingInfo>
      {isConfirmShown && (
        <DialogPortal>
          <Dialog message={'정말 삭제하시겠어요?'}>
            <Dialog.Button
              title="취소"
              onClick={() => {
                setIsConfirmShown(false);
              }}
            />
            <Dialog.Button
              title="삭제"
              onClick={() => {
                setSelectedTowns(selectedTowns.filter(town => town.townId !== selectedTownId));
                setIsConfirmShown(false);
              }}
            />
          </Dialog>
        </DialogPortal>
      )}
      {isAlertShown && (
        <DialogPortal>
          <Dialog message={'최소 1개의 동네를 설정해주세요.'}>
            <Dialog.Button
              title="확인"
              onClick={() => {
                setIsAlertShown(false);
              }}
            />
          </Dialog>
        </DialogPortal>
      )}
    </$FooterContainer>
  );
};

export default TownSettingFooter;